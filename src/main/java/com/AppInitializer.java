package com;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;


public class AppInitializer {

	//@Value("#{'${COINS_DENOMINATOR}'.split(',')}")
	private List<Double> listOfCoinDenom = Stream.of(0.01,0.05,0.10,0.25)
			.collect(Collectors.toCollection(LinkedList::new));

	//@Value("${NO_OF_COINS}")
	private static Integer totalCoins = 100;
	
	private static Map<Integer, Integer> coinsDenominator = new HashMap<>(); 
	
	private int maxAvailableChange = 0;
	
	private Map<Integer, Integer> sortedAvailabeCoins = new TreeMap<>();


	private final Set<Integer> validBills = Stream.of(1, 2, 5, 10, 20, 50, 100)
			.collect(Collectors.toCollection(HashSet::new));


	public Map<Integer, Integer> getSortedAvailabeCoins() {
		return this.sortedAvailabeCoins;
	}
	public void setSortedAvailabeCoins(Map<Integer, Integer> coins) {
		this.sortedAvailabeCoins = coins;
	}

	public synchronized void useAvailabeCoins(Integer coin, Integer number) {
		// TODO validation for key
		this.sortedAvailabeCoins.put(coin, number);
	}

	public void sortAvailabeCoins(String order) {
		if(order.equals("MAX")) {
			Map<Integer, Integer> coins = new TreeMap<>(Collections.reverseOrder());

    		coins.putAll(this.sortedAvailabeCoins);
    		this.setSortedAvailabeCoins(coins);
	
		}else {
			this.setSortedAvailabeCoins( new TreeMap<Integer, Integer>(this.sortedAvailabeCoins));
		}
	}

	public synchronized int getMaxAvailableChange() {
		this.maxAvailableChange = 0;
		this.getSortedAvailabeCoins().forEach((k, v) -> {
			this.maxAvailableChange = this.maxAvailableChange + (k * v);
		});
		return this.maxAvailableChange;
	}

	public Set<Integer> getValidBills() {
		return this.validBills;
	}

	public Integer[] getDenominatorOfCoins() {
		Set<Integer> coins = this.getSortedAvailabeCoins().entrySet().stream().map(Map.Entry::getKey)
				.collect(Collectors.toSet());
		return coins.toArray(new Integer[coins.size()]);

	}
	
	public void initializeCoinsDeno() {
			this.listOfCoinDenom.forEach( coin -> {
				coinsDenominator.put((int) (coin * 100), totalCoins);
			} );
	}
	
	
	 @PostConstruct
	 public void init() {
		 this.initializeCoinsDeno();
		 this.setSortedAvailabeCoins( new TreeMap<Integer, Integer>(this.coinsDenominator));

	 }
	 

}
