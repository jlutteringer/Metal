package org.alloy.metal.test.string;

import org.alloy.metal.object.WithLogger;

public class StringPlaceholderIteratorTest extends WithLogger {
//	@Test
//	public void test() {
//		List<String> testStrings = _List.list();
//		testStrings.add("This is a test string with no placeholders");
//		testStrings.add("This is ${a test string with no placeholder${s");
//		testStrings.add(null);
//		testStrings.add("");
//		testStrings.add("${${${");
//		testStrings.add("}}");
//		testStrings.add("asdfasdf }A asdfa sd} as}");
//
//		testStrings.add("This is a test string with a ${placeholder}");
//		testStrings.add("This is a ${placeholder1} test string with a ${placeholder2}");
//		testStrings.add("This is a ${placeholder1} test string ${} with a ${placeholder3}");
//		testStrings.add("This is a ${placeholder1} test string ${} with a ${placeholder3} asdd${}");
//		testStrings.add("This ${is a ${placeholder1} test string ${}} with a ${placeholder3} asdd${}");
//
//		List<Set<StringPlaceholder>> testResults = _List.list();
//		testResults.add(_Set.set());
//		testResults.add(_Set.set());
//		testResults.add(_Set.set());
//		testResults.add(_Set.set());
//		testResults.add(_Set.set());
//		testResults.add(_Set.set());
//		testResults.add(_Set.set());
//
//		testResults.add(_Set.set(
//				new StringPlaceholder("placeholder", testStrings.get(7).indexOf("${placeholder}"))));
//		testResults.add(_Set.set(
//				new StringPlaceholder("placeholder1", testStrings.get(8).indexOf("${placeholder1}")),
//				new StringPlaceholder("placeholder2", testStrings.get(8).indexOf("${placeholder2}"))));
//		testResults.add(_Set.set(
//				new StringPlaceholder("placeholder1", testStrings.get(9).indexOf("${placeholder1}")),
//				new StringPlaceholder("", testStrings.get(9).indexOf("${}")),
//				new StringPlaceholder("placeholder3", testStrings.get(9).indexOf("${placeholder3}"))));
//		testResults.add(_Set.set(
//				new StringPlaceholder("placeholder1", testStrings.get(10).indexOf("${placeholder3}")),
//				new StringPlaceholder("", testStrings.get(10).indexOf("${}")),
//				new StringPlaceholder("placeholder3", testStrings.get(10).indexOf("${placeholder3}")),
//				new StringPlaceholder("", testStrings.get(10).lastIndexOf("${}"))));
//		testResults.add(_Set.set(
//				new StringPlaceholder("placeholder1", testStrings.get(11).indexOf("${placeholder3}")),
//				new StringPlaceholder("", testStrings.get(11).indexOf("${}")),
//				new StringPlaceholder("placeholder3", testStrings.get(11).indexOf("${placeholder3}")),
//				new StringPlaceholder("", testStrings.get(11).lastIndexOf("${}"))));
//
//		int count = 0;
//		for (String testString : testStrings) {
//			logger.info("Testing string {}", testString);
//			Iterable<StringPlaceholder> resultsSet = _Iterable.fromIteratorSupplier(() -> new StringPlaceholderIterator("${", "}", testString));
//			boolean compare = _Collection.compareElements(
//					_List.list(resultsSet), testResults.get(count), _Equality.comparator(StringPlaceholder.comparator()), false);
//			Assert.assertTrue(compare);
//			count++;
//		}
//	}
}