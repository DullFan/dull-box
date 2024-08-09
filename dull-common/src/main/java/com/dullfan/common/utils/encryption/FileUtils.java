package com.dullfan.common.utils.encryption;

import cn.hutool.core.date.DateUtil;
import com.dullfan.common.constant.Constants;
import com.dullfan.common.utils.uuid.IdUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

/**
 * 公用的文件工具类
 */
public class FileUtils {

    /**
     * 获取文件的后缀
     */
    public static String getFileSuffix(String filename) {
        if (StringUtils.isBlank(filename) || filename.lastIndexOf(Constants.POINT_STR) == Constants.MINUS_ONE_INT) {
            return Constants.EMPTY_STR;
        }
        return filename.substring(filename.lastIndexOf(Constants.POINT_STR)).toLowerCase();
    }

    /**
     * 获取文件的类型
     */
    public static String getFileExtName(String filename) {
        if (StringUtils.isBlank(filename) || filename.lastIndexOf(Constants.POINT_STR) == Constants.MINUS_ONE_INT) {
            return Constants.EMPTY_STR;
        }
        return filename.substring(filename.lastIndexOf(Constants.POINT_STR) + Constants.ONE_INT).toLowerCase();
    }

    private static final String[] SIZE_UNITS = {"B", "KB", "MB", "GB", "TB"};

    /**
     * 通过文件大小转化文件大小的展示名称，带小数点
     */
    public static String byteCountToDisplaySize(Long totalSize) {
        if (totalSize == null) {
            return "";
        }

        if (totalSize < 1024) {
            return totalSize + " B";
        }

        int digitGroups = (int) (Math.log10(totalSize) / Math.log10(1024));
        double size = totalSize / Math.pow(1024, digitGroups);

        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(size) + " " + SIZE_UNITS[digitGroups];
    }

    /**
     * 批量删除物理文件
     */
    public static void deleteFiles(List<String> realFilePathList) throws IOException {
        if (CollectionUtils.isEmpty(realFilePathList)) {
            return;
        }
        for (String realFilePath : realFilePathList) {
            org.apache.commons.io.FileUtils.forceDelete(new File(realFilePath));
        }
    }

    /**
     * 生成文件的存储路径
     * <p>
     * 生成规则：基础路径 + 年 + 月 + 日 + 随机的文件名称
     */
    public static String generateStoreFileRealPath(String basePath, String filename) {
        return basePath +
                File.separator +
                DateUtil.thisYear() +
                File.separator +
                (DateUtil.thisMonth() + 1) +
                File.separator +
                DateUtil.thisDayOfMonth() +
                File.separator +
                IdUtils.fastUUID() +
                getFileSuffix(filename);

    }

    /**
     * 将文件的输入流写入到文件中
     * 使用底层的sendfile零拷贝来提高传输效率
     */
    public static void writeStream2File(InputStream inputStream, File targetFile, Long totalSize) throws IOException {
        createFile(targetFile);
        RandomAccessFile randomAccessFile = new RandomAccessFile(targetFile, "rw");
        FileChannel outputChannel = randomAccessFile.getChannel();
        ReadableByteChannel inputChannel = Channels.newChannel(inputStream);
        outputChannel.transferFrom(inputChannel, 0L, totalSize);
        inputChannel.close();
        outputChannel.close();
        randomAccessFile.close();
        inputStream.close();
    }

    /**
     * 创建文件
     * 包含父文件一起视情况去创建
     */
    public static void createFile(File targetFile) throws IOException {
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        targetFile.createNewFile();
    }

    /**
     * 生成文件分片的存储路径
     * <p>
     * 生成规则：基础路径 + 年 + 月 + 日 + 唯一标识 + 随机的文件名称 + __,__ + 文件分片的下标
     */
    public static String generateStoreFileChunkRealPath(String basePath, String identifier, Integer chunkNumber) {
        return basePath +
                File.separator +
                DateUtil.thisYear() +
                File.separator +
                (DateUtil.thisMonth() + 1) +
                File.separator +
                DateUtil.thisDayOfMonth() +
                File.separator +
                identifier +
                File.separator +
                IdUtils.fastUUID() +
                Constants.COMMON_SEPARATOR +
                chunkNumber;
    }

    /**
     * 追加写文件
     */
    public static void appendWrite(Path target, Path source) throws IOException {
        Files.write(target, Files.readAllBytes(source), StandardOpenOption.APPEND);
    }

    /**
     * 利用零拷贝技术读取文件内容并写入到文件的输出流中
     */
    public static void writeFile2OutputStream(FileInputStream fileInputStream, OutputStream outputStream, long length) throws IOException {
        FileChannel fileChannel = fileInputStream.getChannel();
        WritableByteChannel writableByteChannel = Channels.newChannel(outputStream);
        fileChannel.transferTo(Constants.ZERO_LONG, length, writableByteChannel);
        outputStream.flush();
        fileInputStream.close();
        outputStream.close();
        fileChannel.close();
        writableByteChannel.close();
    }

    /**
     * 普通的流对流数据传输
     */
    public static void writeStream2StreamNormal(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != Constants.MINUS_ONE_INT) {
            outputStream.write(buffer, Constants.ZERO_INT, len);
        }
        outputStream.flush();
        inputStream.close();
        outputStream.close();
    }

}
