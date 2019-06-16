package fr.lsmbo.msda.spectra.comp.model;

import java.sql.SQLException;

import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.db.DataSource;
import fr.lsmbo.msda.spectra.comp.io.PeakListProvider;

public class ViewModel {
	public void compare() {
		PeakListProvider.compareSpectra();
	}

	public void loadFirstPkl(String projectName,String firstPklList){
		try {
			Session.USER_PARAMS.setDataSource("file");
			PeakListProvider.loadFirstSpectra(projectName, firstPklList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadSecondPkl(String projectName,String secondPklList)  {
		try {
			Session.USER_PARAMS.setDataSource("file");
			PeakListProvider.loadSecondSpectra(projectName, secondPklList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
