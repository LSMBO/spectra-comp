# Spectra-comp
Spectra-comp is an Ms/Ms spectra comparison tool. It used to compare between reference spectra and test spectra using dot product method. The reference spectra could be loaded from:
* Proline project (you should have proline software installed and a proline account as well https://github.com/profiproteomics). 
* Peaklist file (<code>.mgf or .pkl</code>).

The tested spectra could be loaded from :

* Proline project (you should have proline software installed and a proline account as well https://github.com/profiproteomics).
* Peaklist file (<code>.mgf or .pkl</code>).

The spectra are compared using a dot product method. The user could set up the comaprison spectra parameters from the graphical interface or from the paremeters JSON file (<code>default-params.json</code>).
The parameters of comparison are :
* Precursor delta Moz: the delta (+- Moz), it will be used for the precursor moz.
* Fragment delta Moz: the delta (+- Moz), it will be used for the fragment moz.
* Delta retention time: the delta retention time (-+ in secondes).  
* Number of minimum peaks: the minimum number of peaks that matched between the reference spectrum and the tested spectrum.  
* Number of peaks: the number of peaks that matched: for the tested spectra if a value is present for the same peak of reference spectra, we keep the most intense value of intensity.   
* Minimum theta: this value is computed as <code>Math.cos(Math.toRadians(thetaMin))</code>  

 <code> Cos theta = ∑NB_PEAKS(√RS.peak * √TS.peak)/(√(∑NB_PEAKS(RS.peak))*√(∑NB_PEAKS(TS.peak)))</code> 
 
 <code>TS.peak = intensity of peaks TS(tested spectra)</code>  <br> <code> RS.peak = intensity of peaks RS(reference spectra)</code>
 
**Usage**

Spectra-comp has been developped in Java and can be used on Windows and Linux.<br>
Spectra-comp requires Java 8 at least.<br>
To run Spectra-comp, you can double-click on the script <code>start_spectra_comp</code>

 <h3>Screenshot</h3>

![alt text](https://github.com/LSMBO/spectra-comp/blob/master/src/main/resources/images/screen-shot.png)
