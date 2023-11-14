package com.assignment3.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;

public class WordCount {

    static class Filter implements FlatMapFunction<String, String> {
        @Override
        public Iterator<String> call(String s) {
            /*
             * add your code to filter words
             */
            String[] subStrings = s.split("\\s+");
            return Arrays.asList(subStrings).iterator();
        }

    }

    public static void main(String[] args) {
        String ip = "172.20.10.5";
        String textFilePath = "hdfs://" + ip + ":9000/sparkApp/input/pigs.txt"; // update to HDFS url for task2
        // task2: update the setMaster with your cluster master URL for executing this code on the cluster
        SparkConf conf = new SparkConf().setAppName("WordCountWithSpark").setMaster("spark://172.20.10.5:7077");
        conf.set("spark.hadoop.validateOutputSpecs", "false");
        JavaSparkContext sparkContext = new JavaSparkContext(conf);
        JavaRDD<String> textFile = sparkContext.textFile(textFilePath);
        JavaRDD<String> words = textFile.flatMap(new Filter());

        // count how often each word appears
        // The first value of the tuple is the key, the second value is the value
        // The reduceByKey function is explained here:
        // https://stackoverflow.com/questions/50248695/understanding-javapairrdd-reducebykey-function
        JavaPairRDD<String, Integer> counts = words.mapToPair(word -> new Tuple2<>(word, 1)).reduceByKey((a, b) -> a + b);

        // sort counts by value descending
        // Sorting by value is not supported (see https://issues.apache.org/jira/browse/SPARK-3655)
        // We swap the key and value, sort by key, and swap back
        counts = counts.mapToPair(x -> x.swap()).sortByKey(false).mapToPair(x -> x.swap());

        // save the output in the format word:count in output/output.txt
        // saveAsTextFile function is explained here:
        // https://www.tabnine.com/code/java/methods/org.apache.spark.api.java.JavaPairRDD/saveAsTextFile
        counts.coalesce(1).saveAsTextFile("hdfs://" + ip + ":9000/sparkApp/input/output.txt");

        // wait for 30 seconds before terminating the program for screenshot
        // try {
        //     Thread.sleep(30000);
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }

        sparkContext.stop();
        sparkContext.close();
    }
}

