package com.coderscampus.assignment;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class ProcessData {

	public void fetch(ExecutorService service, List<CompletableFuture<List<Integer>>> list, Assignment8 assignment) {

		for (int i = 0; i < 1000; i++) {
			CompletableFuture<List<Integer>> numbersList = CompletableFuture.supplyAsync(() -> assignment.getNumbers(),
					service);
			list.add(numbersList);

		}

		// Keep the thread alive
		while (list.stream().filter(CompletableFuture::isDone).count() < 1000) {
		}
	}

	public void addToList(List<CompletableFuture<List<Integer>>> list, List<Integer> newList) {

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

		});

	}

	public void printResult(ExecutorService service, List<Integer> list) {
		// Run it in a different thread but a single one because we only need one
		// transaction
		service.submit(() -> {
			Map<String, Long> map = list.stream()
					.collect(Collectors.groupingBy(i -> i.toString(), Collectors.counting()));
			Set<Entry<String, Long>> entrySet = map.entrySet();
			System.out.println(entrySet);

			// shutdown the thread after 1 transaction
			service.shutdownNow();

		});
	}

}
