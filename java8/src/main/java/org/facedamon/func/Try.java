package org.facedamon.func;

import org.facedamon.inter.BufferedReaderProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author damon
 * @desc 释放资源, 行为参数化
 * @date 2021/5/4
 */
public class Try {
    // 一次读一行
    public static String processFile() throws IOException {
        //自动释放资源
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            return br.readLine();
        }
    }

    // 一次读多行
    // 使用的时候processFile((BufferedReader br) -> br.readLine()+br.readLine()
    // 1. 定义函数式接口BufferedReaderProcessor
    // 2. 参数化
    public static String processFile(BufferedReaderProcessor p) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            return p.process(br);
        }
    }

    public static void main(String[] args) throws IOException {
        //processFile((BufferedReader br) -> br.readLine());
        processFile((BufferedReader::readLine));
        //处理两行
        processFile((BufferedReader br) -> br.readLine() + br.readLine());
    }
}
