package gee.sudoku;

import gee.sudoku.krn.Cell;
import gee.sudoku.krn.MatriceZone;
import junit.framework.TestCase;

public class MatriceZoneTest extends TestCase {
	MatriceZone test;

	protected void setUp() throws Exception {

	}

	protected void tearDown() throws Exception {
	}

	public void testFindChoiceCell() {
		test = new MatriceZone("Test");
		test.addCell(new Cell(9, 0, 0, 9));
		test.addCell(new Cell(9, 0, 0, 7));
		test.addCell(new Cell(9, 0, 0, 4, 6));
		test.addCell(new Cell(9, 0, 0, 2, 4));
		test.addCell(new Cell(9, 0, 0, 8));
		test.addCell(new Cell(9, 0, 0, 1, 3));
		test.addCell(new Cell(9, 0, 0, 5));
		test.addCell(new Cell(9, 0, 0, 2, 6));
		test.addCell(new Cell(9, 0, 0, 1, 2, 3));
		System.out.println(test);
		assertEquals(1, test.findChoice(new Cell(9, 0, 0, 2, 4)).size());
		assertTrue(true);
	}

	public void testFindChoiceIntArray() {
		test = new MatriceZone("Test");
		test.addCell(new Cell(9, 0, 0, 9));
		test.addCell(new Cell(9, 0, 0, 7));
		test.addCell(new Cell(9, 0, 0, 4, 6));
		test.addCell(new Cell(9, 0, 0, 2, 4));
		test.addCell(new Cell(9, 0, 0, 8));
		test.addCell(new Cell(9, 0, 0, 1, 3));
		test.addCell(new Cell(9, 0, 0, 5));
		test.addCell(new Cell(9, 0, 0, 2, 6));
		test.addCell(new Cell(9, 0, 0, 1, 2, 3));
		System.out.println(test);
		assertEquals(1, test.findChoice(2, 4).size());
	}

	public void testFindCellsWithChoices() {
		test = new MatriceZone("Test");
		test.addCell(new Cell(9, 0, 0, 9));
		test.addCell(new Cell(9, 0, 0, 7));
		test.addCell(new Cell(9, 0, 0, 4, 6));
		test.addCell(new Cell(9, 0, 0, 2, 4));
		test.addCell(new Cell(9, 0, 0, 8));
		test.addCell(new Cell(9, 0, 0, 1, 3));
		test.addCell(new Cell(9, 0, 0, 5));
		test.addCell(new Cell(9, 0, 0, 2, 6));
		test.addCell(new Cell(9, 0, 0, 1, 2, 3));
		System.out.println(test);
		assertEquals(1, test.findCellsWithChoices(2, 4).size());
	}

	public void testFindCellsWithTwoOrMoreChoices() {
		test = new MatriceZone("Test");
		test.addCell(new Cell(9, 0, 0, 7));
		test.addCell(new Cell(9, 0, 0, 4));
		test.addCell(new Cell(9, 0, 0, 1, 3));
		test.addCell(new Cell(9, 0, 0, 2));
		test.addCell(new Cell(9, 0, 0, 6));
		test.addCell(new Cell(9, 0, 0, 9));
		test.addCell(new Cell(9, 0, 0, 1, 3, 5, 8));
		test.addCell(new Cell(9, 0, 0, 3, 8));
		test.addCell(new Cell(9, 0, 0, 1, 3, 5));
		System.out.println(test);
		assertEquals(3, test.findCellsWithTwoOrMoreChoices(1, 3, 5).size());
	}

	public void testFindCellsWithTwoOrMoreChoices2() {
		test = new MatriceZone("Test");
		test.addCell(new Cell(9, 0, 0, 1, 4, 6));
		test.addCell(new Cell(9, 0, 0, 1, 6, 9));
		test.addCell(new Cell(9, 0, 0, 2));
		test.addCell(new Cell(9, 0, 0, 1, 4, 6));
		test.addCell(new Cell(9, 0, 0, 1, 6, 7, 9));
		test.addCell(new Cell(9, 0, 0, 5));
		test.addCell(new Cell(9, 0, 0, 8));
		test.addCell(new Cell(9, 0, 0, 3));
		test.addCell(new Cell(9, 0, 0, 1, 7));
		System.out.println(test);
		assertEquals(3, test.findCellsWithTwoOrMoreChoices(1, 9, 7).size());
	}

	// Looking for 4,6,3 in [0(1,4,6) ][0(1,4,6) ][8 ][2 ][9 ][0(3,5,6) ][7
	// ][0(1,3,5) ][0(1,5) ]
	public void testFindCellsWithTwoOrMoreChoices3() {
		test = new MatriceZone("Test");
		test.addCell(new Cell(9, 0, 0, 1, 4, 6));
		test.addCell(new Cell(9, 0, 0, 1, 4, 6));
		test.addCell(new Cell(9, 0, 0, 8));
		test.addCell(new Cell(9, 0, 0, 2));
		test.addCell(new Cell(9, 0, 0, 9));
		test.addCell(new Cell(9, 0, 0, 3, 5, 6));
		test.addCell(new Cell(9, 0, 0, 7));
		test.addCell(new Cell(9, 0, 0, 1, 3, 5));
		test.addCell(new Cell(9, 0, 0, 1, 5));
		System.out.println(test);
		assertEquals(3, test.findCellsWithTwoOrMoreChoices(4, 6, 3).size());
	}

}
