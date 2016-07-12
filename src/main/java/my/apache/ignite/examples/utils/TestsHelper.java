/** TestsHelper.java -
* @version      $Name$
* @module       my.apache.ignite.examples.utils
* 
* @purpose
* @see
*
* @author   Kamal (kamal@nmsworks.co.in)
*
* @created  Jul 12, 2016
* $Id$
*
* @bugs
*
* Copyright 2016-2017 NMSWorks Software Pvt Ltd. All rights reserved.
* NMSWorks PROPRIETARY/CONFIDENTIAL. Use is subject to licence terms.
*/ 

package my.apache.ignite.examples.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import my.apache.ignite.examples.collocation.Person;
import my.apache.ignite.examples.collocation.PersonKey;

public class TestsHelper {

	private static final String LETTERS_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String NUMBERS_ALPHABET = "0123456789";
	private static final Random RANDOM = new Random(System.currentTimeMillis());
	
	public TestsHelper() {
	}
	
    public static String randomString(int len) {
        StringBuilder builder = new StringBuilder(len);

        for (int i = 0; i < len; i++)
            builder.append(LETTERS_ALPHABET.charAt(RANDOM.nextInt(LETTERS_ALPHABET.length())));

        return builder.toString();
    }
    
    public static String randomNumber(int len) {
        StringBuilder builder = new StringBuilder(len);

        for (int i = 0; i < len; i++)
            builder.append(NUMBERS_ALPHABET.charAt(RANDOM.nextInt(NUMBERS_ALPHABET.length())));

        return builder.toString();
    }

	public static Map<String, String> generateStringsEntries(int cnt) {
		Map<String, String> map = new HashMap<>();
		for (int i=0; i<cnt; i++) {
			map.put(Integer.toString(i), randomString(5));
		}
		return map;
	}

	public static Map<? extends PersonKey, ? extends Person> generatePersonKeyPersonsMap(int cnt) {
		Map<PersonKey, Person> map = new HashMap<>();
		for (int i=0; i<cnt; i++) {
			PersonKey key = generateRandomPersonKey();
			Person person = generateRandomPerson(key.getName(), key.getCompanyId());
			map.put(key, person);
		}
		return map;
	}
	
	private static PersonKey generateRandomPersonKey() {
		PersonKey key = new PersonKey(randomString(5), Integer.valueOf(randomNumber(3)));
		return key;
	}
	
	private static Person generateRandomPerson(String name, int compId) {
		Person person = new Person(name, Integer.valueOf(randomNumber(2)), Integer.valueOf(randomNumber(3)), compId);
		return person;
	}
}


/**
 * $Log$
 *  
 */
