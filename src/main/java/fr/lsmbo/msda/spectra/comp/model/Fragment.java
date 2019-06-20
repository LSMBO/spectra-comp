
package fr.lsmbo.msda.spectra.comp.model;

// TODO: Auto-generated Javadoc
/**
 * This class implements the functionality for a fragment.
 * 
 * @author Aromdhani
 *
 */
public class Fragment {

	/** The id. */
	private Integer id = 0;
	
	/** The mz. */
	private Double mz = 0D;
	
	/** The intensity. */
	private float intensity = 0;
	
	/** The charge. */
	private Integer charge = 1;

	/**
	 * Default constructor.
	 */
	public Fragment() {
		super();
	}

	/**
	 * Instantiates a new fragment.
	 *
	 * @param id            the fragment id
	 * @param mz            the fragment Moz
	 * @param intensity            the fragment intensity
	 */
	public Fragment(Integer id, Double mz, float intensity) {
		super();
		this.id = id;
		this.mz = mz;
		this.intensity = intensity;
	}

	/**
	 * Instantiates a new fragment.
	 *
	 * @param id            the fragment id
	 * @param mz            the fragment mz
	 * @param intensity            the fragment intensity
	 * @param charge            the fragment charge
	 */
	public Fragment(Integer id, Double mz, float intensity, Integer charge) {
		super();
		this.id = id;
		this.mz = mz;
		this.intensity = intensity;
		this.charge = charge;
	}

	/**
	 * Gets the charge.
	 *
	 * @return the fragment charge
	 */
	public Integer getCharge() {
		return charge;
	}

	/**
	 * Gets the id.
	 *
	 * @return the fragement id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Gets the intensity.
	 *
	 * @return the fragment intensity
	 */
	public float getIntensity() {
		return intensity;
	}

	/**
	 * Gets the mz.
	 *
	 * @return the fragment Mz
	 */
	public Double getMz() {
		return mz;
	}

	/**
	 * Sets the charge.
	 *
	 * @param charge            the fragment charge to set
	 */
	public void setCharge(Integer charge) {
		this.charge = charge;
	}

	/**
	 * Sets the id.
	 *
	 * @param id            teh fragment id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Sets the intensity.
	 *
	 * @param intensity            the fragment intensity to set
	 */
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	/**
	 * Sets the mz.
	 *
	 * @param mz            the fragment mz to set
	 */
	public void setMz(Double mz) {
		this.mz = mz;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder strBldr = new StringBuilder();
		strBldr.append("#Fragment id:").append(id).append(" ;moz: ").append(mz).append(" ;intensity: ")
				.append(intensity).append(" ;charge: ").append(charge);
		return strBldr.toString();
	}

}
