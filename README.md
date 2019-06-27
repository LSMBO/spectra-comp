# Spectra-comp
Spectra-comp is an Ms/Ms spectra comparison tool. The reference spectra could be loaded from:
* Proline project (you should have proline software installed and a proline account as well https://github.com/profiproteomics). 
* Peaklist file (<code>.mgf or .pkl</code>).

The tested spectra could be loaded from :

* Proline project. 
* Peaklist file (<code>.mgf or .pkl</code>).

The spectra are compared using a dot product method. The user could set up the comaprison spectra parameters from the graphical interface or from the paremeters JSON file (<code>default-params.json</code>).
The parameters of comparison are :
* <code>Delta Moz</code>
* <code>Delta retention time</code>   
* <code>Number of minimum peaks</code>    
* <code>Minimum theta</code>    
* <code>Number of peaks</code>   

 <code>TS.peak = intensity of peaks TS(tested spectra)</code> 
 
 <code> RS.peak = intensity of peaks RS(reference spectra)</code> 
 
 <code> Cos theta = ∑NB_PEAKS(√RS.peak * √TS.peak)/(√(∑NB_PEAKS(RS.peak))*√(∑NB_PEAKS(TS.peak)))</code> 
 
 <h2>Description</h2>

![alt text](https://github.com/LSMBO/spectra-comp/blob/master/src/main/resources/images/screen_shot.png)
