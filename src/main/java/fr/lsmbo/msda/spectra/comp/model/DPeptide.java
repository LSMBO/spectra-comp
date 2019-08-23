package fr.lsmbo.msda.spectra.comp.model;

public class DPeptide {
	private Long m_id;
	private String m_sequence;
	private String m_ptm;
	private Double m_calculatedMass;

	public DPeptide() {
	}

	public DPeptide(Long id) {
		m_id = id;
	}

	public DPeptide(Long id, String sequence, Double calculatedMass) {
		m_id = id;
		m_sequence = sequence;
		m_calculatedMass = calculatedMass;
	}

	/**
	 * @return the m_id
	 */
	public final Long getM_id() {
		return m_id;
	}

	/**
	 * @param m_id
	 *            the m_id to set
	 */
	public final void setM_id(Long m_id) {
		this.m_id = m_id;
	}

	/**
	 * @return the m_sequence
	 */
	public final String getM_sequence() {
		return m_sequence;
	}

	/**
	 * @param m_sequence
	 *            the m_sequence to set
	 */
	public final void setM_sequence(String m_sequence) {
		this.m_sequence = m_sequence;
	}

	/**
	 * @return the m_ptm
	 */
	public final String getM_ptm() {
		return m_ptm;
	}

	/**
	 * @param m_ptm
	 *            the m_ptm to set
	 */
	public final void setM_ptm(String m_ptm) {
		this.m_ptm = m_ptm;
	}

	/**
	 * @return the m_calculatedMass
	 */
	public final Double getM_calculatedMass() {
		return m_calculatedMass;
	}

	/**
	 * @param m_calculatedMass
	 *            the m_calculatedMass to set
	 */
	public final void setM_calculatedMass(Double m_calculatedMass) {
		this.m_calculatedMass = m_calculatedMass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DPeptide))
			return false;
		DPeptide peptide = (DPeptide) obj;
		Boolean isEquals = (this.m_id == peptide.m_id) ? true : false;
		return isEquals;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DPeptide [m_id=" + m_id + ", m_sequence=" + m_sequence + ", m_ptm=" + m_ptm + ", m_calculatedMass="
				+ m_calculatedMass + "]";
	}

}
