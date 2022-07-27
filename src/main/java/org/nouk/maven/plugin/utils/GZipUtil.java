package org.nouk.maven.plugin.utils;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class GZipUtil {
    public static void compression(String srcPath, String tarPath,String fileName) throws IOException {
        // 被压缩打包的文件夹
        Path source = Paths.get(srcPath);
        //如果不是文件夹抛出异常
        if (!Files.isDirectory(source)) {
            throw new IOException("please specify a folder");
        }
        //压缩之后的输出文件名称
        String tarFileName = tarPath+ File.separator+ fileName + ".tar.gz";
        //OutputStream输出流、BufferedOutputStream缓冲输出流
        //GzipCompressorOutputStream是gzip压缩输出流
        //TarArchiveOutputStream打tar包输出流（包含gzip压缩输出流）
        try (OutputStream fOut = Files.newOutputStream(Paths.get(tarFileName));
             BufferedOutputStream buffOut = new BufferedOutputStream(fOut);
             GzipCompressorOutputStream gzOut = new GzipCompressorOutputStream(buffOut);
             TarArchiveOutputStream tOut = new TarArchiveOutputStream(gzOut)) {
            //遍历文件目录树
            Files.walkFileTree(source, new SimpleFileVisitor<Path>() {

                //当成功访问到一个文件
                @Override
                public FileVisitResult visitFile(Path file,
                                                 BasicFileAttributes attributes) throws IOException {

                    // 判断当前遍历文件是不是符号链接(快捷方式)，不做打包压缩处理
                    if (attributes.isSymbolicLink()) {
                        return FileVisitResult.CONTINUE;
                    }

                    //获取当前遍历文件名称
                    Path targetFile = source.relativize(file);

                    //将该文件打包压缩
                    TarArchiveEntry tarEntry = new TarArchiveEntry(
                            file.toFile(), targetFile.toString());
                    tOut.putArchiveEntry(tarEntry);
                    Files.copy(file, tOut);
                    tOut.closeArchiveEntry();
                    //继续下一个遍历文件处理
                    return FileVisitResult.CONTINUE;
                }

                //当前遍历文件访问失败
                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    System.err.printf("The file could not be compressed and packaged as tar.gz : %s%n%s%n", file, exc);
                    return FileVisitResult.CONTINUE;
                }

            });
            //for循环完成之后，finish-tar包输出流
            tOut.finish();
        }
    }
}
