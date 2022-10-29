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

		// To fetch data use newFixedThreadPool
		ExecutorService service = Executors.newFixedThreadPool(1000);

		// Only to print the exptected output in a different thread
		ExecutorService service2 = Executors.newSingleThreadExecutor();

		
		
		List<CompletableFuture<List<Integer>>> list = new ArrayList<>();
		for (int i = 0; i < 1000; i++) {
			CompletableFuture<List<Integer>> numbersList = CompletableFuture.supplyAsync(() -> assignment.getNumbers(),
					service);
			list.add(numbersList);

		}

		// Keep the thread alive
		while (list.stream().filter(CompletableFuture::isDone).count() < 1000) {
		}

		
		
		List<Integer> newList = new ArrayList<>();
		list.forEach(item -> {
			// Add List<Integer>'s if CompletableFuture is done
			if (item.isDone()) {

				try {
					newList.addAll(item.get());

				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			
			
			// Sort them to find the occurence of each unique number in the List
			Collections.sort(newList);
			
			// Run it in a different thread but a single one because we only need one transaction
			service2.submit(() -> {
				Map<String, Long> map = newList.stream()
						.collect(Collectors.groupingBy(i -> i.toString(), Collectors.counting()));
				Set<Entry<String, Long>> entrySet = map.entrySet();
				System.out.println(entrySet);
				
				// shutdown the thread after 1 transaction
				service2.shutdownNow();

			});

		});

	}

}
