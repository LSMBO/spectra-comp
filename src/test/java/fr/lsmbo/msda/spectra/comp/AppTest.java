package fr.lsmbo.msda.spectra.comp;

import java.io.File;

import org.junit.Assert;

import fr.lsmbo.msda.spectra.comp.io.PeaklistReader;
import fr.lsmbo.msda.spectra.comp.list.ListOfSpectra;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static TestSuite suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Test Files
	 */
	public void testFile() {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			File refSpectraFile = new File(classLoader.getResource("E_040750.mgf").getFile());
			assertNotNull("The file of reference spectra", refSpectraFile.getName());
			File testSpectraFile = new File(classLoader.getResource("E_040750.mgf").getFile());
			assertNotNull("The file of tested spectra", testSpectraFile.getName());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test the loaded spectra
	 */
	public void testSpectraLoder() {
		// Load Reference Spectra
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			File refSpectraFile = new File(classLoader.getResource("E_040750.mgf").getFile());
			assertNotNull("The file of reference spectra", refSpectraFile.getName());
			PeaklistReader.load(refSpectraFile);
			System.out.println(ListOfSpectra.getFirstSpectra().getSpectraAsObservable().size()
					+ "  spectrum was found. They will be set as the reference spectra.");
			Assert.assertEquals(ListOfSpectra.getFirstSpectra().getSpectraAsObservable().size(), 272);
			PeaklistReader.isSecondPeakList = true;
			File testSpectraFile = new File(classLoader.getResource("E_040777.mgf").getFile());
			assertNotNull("The file of tested spectra", testSpectraFile.getName());
			PeaklistReader.load(testSpectraFile);
			System.out.println(ListOfSpectra.getSecondSpectra().getSpectraAsObservable().size()
					+ "  spectrum was found. They will be tested with the reference spectra.");
			Assert.assertEquals(ListOfSpectra.getSecondSpectra().getSpectraAsObservable().size(), 298);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test the spectra
	 */
	public void testSpectraComparison() {
		// Test Spectra
	}
}
