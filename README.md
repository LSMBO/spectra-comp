# Spectra-comp
Spectra-comp is an Ms/Ms spectra comparison tool. The reference spectra could be loaded from:
* Proline project (you should have proline software installed and a proline account as well https://github.com/profiproteomics). 
* Peaklist file (<code>.mgf or .pkl</code>).

The tested spectra could be loaded from :

* Proline project (you should have proline software installed and a proline account as well https://github.com/profiproteomics).
* Peaklist file (<code>.mgf or .pkl</code>).

The spectra are compared using a dot product method. The user could set up the comaprison spectra parameters from the graphical interface or from the paremeters JSON file (<code>default-params.json</code>).
The parameters of comparison are :
* Delta Moz: the delta (+- Moz), it will be used for the precursor moz and for the fragments moz.
* Delta retention time: the delta retention time (-+ in secondes).  
* Number of minimum peaks: the minimum number of peaks that matched between the reference spectrum and the tested specrum(same Moz and RT) .  
* Number of peaks: the number of peaks that matched.   
* Minimum theta: this value is computed as <code>Math.cos(Math.toRadians(thetaMin))</code>  

 <code> Cos theta = ∑NB_PEAKS(√RS.peak * √TS.peak)/(√(∑NB_PEAKS(RS.peak))*√(∑NB_PEAKS(TS.peak)))</code> 
 
 <code>TS.peak = intensity of peaks TS(tested spectra)</code>  and <code> RS.peak = intensity of peaks RS(reference spectra)</code> 
 
 <h2>Description</h2>

![alt text](https://github.com/LSMBO/spectra-comp/blob/master/src/main/resources/images/screen_shot.png)
