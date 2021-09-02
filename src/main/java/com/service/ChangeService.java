package com.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.AppInitializer;

@Service
public class ChangeService {
	
	@Autowired
	private AppInitializer appInitializer;
	
	private int MAX_AVAILABLE_AMOUNT;

	public String getChangeForBill(Integer bill, String order) throws Exception {
		this.appInitializer.sortAvailabeCoins(order);
		this.MAX_AVAILABLE_AMOUNT = this.appInitializer.getMaxAvailableChange()/100;
		StringBuilder sb = this.parseChange(bill, this.getChange(bill*100));
		return sb.toString();
	}
	
	public StringBuilder parseChange(Integer bill, List<Integer> list) {
		StringBuilder sb = new StringBuilder();
		Map<Integer, Integer> changeMap = new HashMap<>();
		sb.append("Change for given bill ");
		sb.append(bill);
		sb.append(" is as below : \n" );
		list.stream().forEach(k -> changeMap.put(k, Collections.frequency(list, k)));
		for (Map.Entry<Integer,Integer> entry : changeMap.entrySet()) {
			sb.append(entry.getValue());
			sb.append(" coins of ");
			sb.append(Double.parseDouble(entry.getKey().toString())/100);
			sb.append("\n");
		}
		return sb;
	}
	
	public List<Integer> getChange(int bill) throws Exception{
		List<Integer> change = new LinkedList<>();
		if (bill <= 0 || bill/100 > this.MAX_AVAILABLE_AMOUNT) {
			
			//TODO throw proper exception and block the execution
			throw new Exception("Insufficient Change.");
		}
		for (Map.Entry<Integer,Integer> entry : this.appInitializer.getSortedAvailabeCoins().entrySet()) {
			int k = entry.getKey();
			int v = entry.getValue();
			synchronized (this) {
				if( v > 0) {
					if (bill/k > 0 ) {
						if(bill/k <= v && bill % k == 0) {
							for(int i=0; i < bill/k; i++) {
								change.add(k);
							}
							this.appInitializer.useAvailabeCoins(k, v - (bill/k));
							bill = bill - (k * bill/k);
							break;
						}
						else if(bill/k <= v && bill % k != 0) {
							for(int i=0; i < bill/k; i++) {
								change.add(k);
							}
							this.appInitializer.useAvailabeCoins(k, v - (bill/k));
							bill = bill % k;
							continue;
						}
						else {
							int tmpBill = (k * v) / k;
							for(int i=0; i < tmpBill; i++) {
								change.add(k);
							}
							this.appInitializer.useAvailabeCoins(k,  v - tmpBill);
							//bill = bill % k ;
							bill = bill - tmpBill * k;
							continue;
						}
					}
				}
			}
		}
		return change;
	}
}
