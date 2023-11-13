Assignment 3
------------

# Team Members
- Leon Luca Klaus Muscat
- Felix Kappeler
- Max Beringer
# GitHub link to your repository (if submitting through GitHub)

> https://github.com/Maxinio-berincini/HSG-HS23-DS-Assignment3

# Task 2

> In Task 1, the Runtime of WordCount is 6.181 seconds.
> 
> In Task 2, the Runtime of WordCount is 10.407 seconds.
> 
> This makes a performance difference of 4.226 seconds.
> 
> We think the client mode was faster, because we are only computing a small dataset.
> When using a cluster there is always some overhead, because the data has to be sent to the cluster and back. 
> This data transfer requires time and thus making the client faster for small datasets.
> If we would have a bigger dataset, the cluster mode would be faster, as it has more resources to compute the data.
> This means the cluster mode is faster from the point on, where the overhead of the data transfer is smaller than the time saved by the additional resources.

# Task 3

1. How does Spark optimize its file access compared to the file access in MapReduce?
> Ans:
> 
> Spark computes the data in memory, while MapReduce writes the data to the disk after each map or reduce action.
> This makes Spark faster, as it is caching the data. Now the bottleneck is no longer the disk speed but the memory speed, which is much faster.

2. In your implementation of WordCount (task1), did you use ReduceByKey or groupByKey method? 
   What does your preferred method do in your implementation? 
   What are the differences between the two methods in Spark?
> Ans: 
> 
> We used the reduceByKey method.
> ReduceByKey combines the values of each key using specified reduce function and also combines the output locally on every partition before shuffling the data.
> After the shuffle, the data is combined again using the same reduce function, to reduce all the values from each partition to produce the final output.
> 
> GroupByKey on the other hand, the key value pairs do not get combined locally, and because of that there is a lot more data that needs to be shuffled, which can become a problem for large datasets.

3. Explain what Resilient Distributed Dataset (RDD) is and the benefits it brings to the classic MapReduce model.
> Ans: 
> 
> RDD is a collection of data that is partitioned across the nodes of a cluster.
> This allows spark to process lots of data simultaneously. (Parallel processing)
> It is resilient, as it is able to reconstruct lost data.
> 
> Because RDD is primarily operating in memory, it is much faster than MapReduce.
> It also allows for iterative processing, which is not possible with MapReduce.
> Additionally RDD provides an easier way to deal with failures, as it is able to reconstruct lost data.

4. Imagine that you have a large dataset that needs to be processed in parallel. 
   How would you partition the dataset efficiently and keep control over the number of outputs created at the end of the execution?
> Ans: 
> 
> We would partition the dataset by the number of nodes in the cluster, so that each node gets the same amount of data.
> This way we can ensure that the data is processed in parallel.
> We also have to make sure, that the partitions are evenly distributed, so that no node is overloaded.
> 
> We can control the number of outputs by using Sparks `coalesce` method.
> `coalesce` reduces the number of files without shuffling the data.

  If a task is stuck on the Spark cluster due to a network issue that the cluster had during execution, 
  which methods can be used to retry or restart the task execution on a node?
> Ans: 
> 
> Spark has a built-in mechanism to automatically retry the task execution on a node.
> Spark lets us configure the number of retries and the conditions for a retry.
