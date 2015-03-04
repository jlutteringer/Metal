package org.alloy.metal.base.main;

import java.math.BigInteger;

import org.alloy.metal.string._String;
import org.alloy.metal.utility._Exception;
import org.alloy.metal.utility._Hash;
import org.alloy.metal.utility._Range;

import com.google.common.primitives.Ints;

public class Main {
	static private int num0 = 0;
	static private int num1 = 0;

	public static void main(String[] args) {
		_Range.range(0, 25).forEach((index) -> {
			num0 = 0;
			num1 = 0;
			_Range.range(0, 400_000)
					.map((number) -> _String.randomString(32) + ".worker1Mobile Conversion")
					.map((randomString) -> _Exception.propagate(() -> randomString.getBytes("UTF-8")))
					.map(_Hash::getMd5Hash)
					.map((bytes) -> new BigInteger(String.valueOf(Ints.fromByteArray(bytes))))
					// .map((bytes) -> new BigInteger(16, bytes))
					.map((bigNum) -> bigNum.mod(BigInteger.valueOf(2)).intValue())
					.forEach((value) -> {
						if (value == 0) {
							num0++;
						}
							else if (value == 1) {
								num1++;
							}
							else {
								throw new RuntimeException();
							}
						});

			System.out.println("Number of 0's - " + num0 + " | Number of 1's - " + num1);
		});
	}
}
