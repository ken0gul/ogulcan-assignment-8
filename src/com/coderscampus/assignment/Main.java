package com.coderscampus.assignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Main {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		Assignment8 assignment = new Assignment8();
		ProcessData data = new ProcessData();
		// To fetch data use newFixedThreadPool
		ExecutorService service = Executors.newFixedThreadPool(1000);

		// Only to print the exptected output in a different thread
		ExecutorService service2 = Executors.newSingleThreadExecutor();

		List<CompletableFuture<List<Integer>>> list = new ArrayList<>();
		data.fetch(service, list, assignment);

		List<Integer> newList = new ArrayList<>();

		// Get the value without blocking the main thread and store it into a new List
		data.addToList(list, newList);

		// Sort them to find the occurence of each unique number in the List
		Collections.sort(newList);

		// Print the expected output using a different single thread
		data.printResult(service2, newList);

	};

}
