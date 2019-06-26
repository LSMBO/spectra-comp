package fr.lsmbo.msda.spectra.comp.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * The class for the dataset from database table.
 * 
 */

public class Dataset {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * The Enum DatasetType.
	 */
	public enum DatasetType {

		/** The identification. */
		IDENTIFICATION,
		/** The quantitation. */
		QUANTITATION,
		/** The aggregate. */
		AGGREGATE,
		/** The trash. */
		TRASH,
		/** The quantitation folder. */
		QUANTITATION_FOLDER,
		/** The identification folder. */
		IDENTIFICATION_FOLDER
	};

	/** The id. */
	private long id;

	/** The description. */
	private String description;

	/** The child count. */
	private int childCount;

	/** The keywords. */
	private String keywords;

	/** The modification log. */
	private String modificationLog;

	/** The name. */
	private String name;

	/** The type. */
	private DatasetType type;

	/** The number. */
	private int number;

	/** The project. */
	private Project project;

	/** The parent dataset. */
	private Dataset parentDataset;

	/** The children. */
	private List<Dataset> children;

	/** The result set id. */
	private Long resultSetId;

	/** The result summary id. */
	private Long resultSummaryId;

	/** The parent dataset id. */
	private Long parentDatasetId;

	/**
	 * Instantiates a new dataset.
	 */
	public Dataset() {
	}

	/**
	 * Instantiates a new dataset.
	 *
	 * @param id
	 *            the id
	 * @param description
	 *            the description
	 * @param childCount
	 *            the child count
	 * @param name
	 *            the name
	 * @param number
	 *            the number
	 * @param parentDatasetId
	 *            the parent dataset id
	 * @param resultSetId
	 *            the result set id
	 * @param resultSummaryId
	 *            the result summary id
	 */
	public Dataset(long id, String description, int childCount, String name, int number, Long parentDatasetId,
			Long resultSetId, Long resultSummaryId) {
		super();
		this.id = id;
		this.description = description;
		this.childCount = childCount;
		this.name = name;
		this.number = number;
		this.parentDatasetId = parentDatasetId;
		this.resultSetId = resultSetId;
		this.resultSummaryId = resultSummaryId;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param pId
	 *            the new id
	 */
	public void setId(final long pId) {
		id = pId;
	}

	/**
	 * Gets the children count.
	 *
	 * @return the children count
	 */
	public int getChildrenCount() {
		return childCount;
	}

	/**
	 * Sets the children count.
	 *
	 * @param pChildrenCount
	 *            the new children count
	 */
	public void setChildrenCount(final int pChildrenCount) {
		childCount = pChildrenCount;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description
	 *            the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public DatasetType getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type
	 *            the new type
	 */
	public void setType(DatasetType type) {
		this.type = type;
	}

	/**
	 * Gets the parent dataset.
	 *
	 * @return the parent dataset
	 */
	public Dataset getParentDataset() {
		return parentDataset;
	}

	/**
	 * Sets the parent dataset.
	 *
	 * @param parentDataset
	 *            the new parent dataset
	 */
	public void setParentDataset(Dataset parentDataset) {
		this.parentDataset = parentDataset;
	}

	/**
	 * Gets the result set id.
	 *
	 * @return the result set id
	 */
	public Long getResultSetId() {
		return resultSetId;
	}

	/**
	 * Sets the result set id.
	 *
	 * @param pResultSetId
	 *            the new result set id
	 */
	public void setResultSetId(final Long pResultSetId) {
		resultSetId = pResultSetId;
	}

	/**
	 * Gets the result summary id.
	 *
	 * @return the result summary id
	 */
	public Long getResultSummaryId() {
		return resultSummaryId;
	}

	/**
	 * Sets the result summary id.
	 *
	 * @param pResultSummaryId
	 *            the new result summary id
	 */
	public void setResultSummaryId(final Long pResultSummaryId) {
		resultSummaryId = pResultSummaryId;
	}

	/**
	 * Gets the keywords.
	 *
	 * @return the keywords
	 */
	public String getKeywords() {
		return this.keywords;
	}

	/**
	 * Sets the keywords.
	 *
	 * @param keywords
	 *            the new keywords
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/**
	 * Gets the modification log.
	 *
	 * @return the modification log
	 */
	public String getModificationLog() {
		return this.modificationLog;
	}

	/**
	 * Sets the modification log.
	 *
	 * @param modificationLog
	 *            the new modification log
	 */
	public void setModificationLog(String modificationLog) {
		this.modificationLog = modificationLog;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the number.
	 *
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Sets the number.
	 *
	 * @param pNumber
	 *            the new number
	 */
	public void setNumber(final int pNumber) {
		number = pNumber;
	}

	/**
	 * Gets the project.
	 *
	 * @return the project
	 */
	public Project getProject() {
		return this.project;
	}

	/**
	 * Sets the project.
	 *
	 * @param project
	 *            the new project
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * Gets the children.
	 *
	 * @return the children
	 */
	public List<Dataset> getChildren() {
		return children;
	}

	/**
	 * Sets the children.
	 *
	 * @param children
	 *            the new children
	 */
	public void setChildren(final List<Dataset> children) {
		this.children = children;
	}

	/**
	 * Adds the child.
	 *
	 * @param child
	 *            the child
	 */
	public void addChild(Dataset child) {
		List<Dataset> childrenList = getChildren();
		if (childrenList == null) {
			childrenList = new ArrayList<Dataset>(1);
			setChildren(childrenList);
		}
		childrenList.add(child);
		child.setNumber(childCount);
		childCount++;
		child.setParentDataset(this);
	}

	/**
	 * Insert child.
	 *
	 * @param child
	 *            the child
	 * @param index
	 *            the index
	 */
	public void insertChild(Dataset child, int index) {
		List<Dataset> childrenList = getChildren();
		if (childrenList == null) {
			childrenList = new ArrayList<Dataset>(1);
			setChildren(childrenList);
		}
		childrenList.add(index, child);
		child.setNumber(index);
		childCount++;

		for (int i = index + 1; i < childrenList.size(); i++) {
			childrenList.get(i).setNumber(i);
		}
		child.setParentDataset(this);
	}

	/**
	 * Replace all children.
	 *
	 * @param newChildren
	 *            the new children
	 */
	public void replaceAllChildren(List<Dataset> newChildren) {
		List<Dataset> childrenList = getChildren();
		if (childrenList != null) {
			Iterator<Dataset> it = childrenList.iterator();
			while (it.hasNext()) {
				Dataset child = it.next();
				child.setParentDataset(null);
				child.setNumber(0);
			}
			childrenList.clear();
			childCount = 0;
		}
		Iterator<Dataset> it = newChildren.iterator();
		while (it.hasNext()) {
			Dataset newChild = it.next();
			addChild(newChild);
		}
	}

	/**
	 * Gets the identification datasets.
	 *
	 * @return the identification datasets
	 */
	public Set<Dataset> getIdentificationDatasets() {
		Set<Dataset> idfDS = new HashSet<Dataset>();
		if ((getChildren() == null || getChildren().isEmpty()) && (getType().equals(DatasetType.IDENTIFICATION))) {
			idfDS.add(this);
		}

		for (Dataset ds : getChildren()) {
			idfDS.addAll(ds.getIdentificationDatasets());
		}
		return idfDS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

}
