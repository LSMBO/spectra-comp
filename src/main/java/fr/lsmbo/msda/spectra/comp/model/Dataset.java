package fr.lsmbo.msda.spectra.comp.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * The class for the dataset from database table.
 * 
 */

public class Dataset {

	private static final long serialVersionUID = 1L;

	public enum DatasetType {
		IDENTIFICATION, QUANTITATION, AGGREGATE, TRASH, QUANTITATION_FOLDER, IDENTIFICATION_FOLDER
	};

	private long id;
	private String description;
	private int childCount;
	private String keywords;
	private String modificationLog;
	private String name;
	private DatasetType type;
	private int number;

	private Project project;
	private Dataset parentDataset;
	private List<Dataset> children;
	private Long resultSetId;
	private Long resultSummaryId;
	private Long parentDatasetId;

	public Dataset() {
	}

	/**
	 * @param id
	 * @param description
	 * @param childCount
	 * @param name
	 * @param type
	 * @param number
	 * @param project
	 * @param parentDatasetId
	 * @param resultSetId
	 * @param resultSummaryId
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

	public long getId() {
		return id;
	}

	public void setId(final long pId) {
		id = pId;
	}

	public int getChildrenCount() {
		return childCount;
	}

	public void setChildrenCount(final int pChildrenCount) {
		childCount = pChildrenCount;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DatasetType getType() {
		return type;
	}

	public void setType(DatasetType type) {
		this.type = type;
	}

	public Dataset getParentDataset() {
		return parentDataset;
	}

	public void setParentDataset(Dataset parentDataset) {
		this.parentDataset = parentDataset;
	}

	public Long getResultSetId() {
		return resultSetId;
	}

	public void setResultSetId(final Long pResultSetId) {
		resultSetId = pResultSetId;
	}

	public Long getResultSummaryId() {
		return resultSummaryId;
	}

	public void setResultSummaryId(final Long pResultSummaryId) {
		resultSummaryId = pResultSummaryId;
	}

	public String getKeywords() {
		return this.keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getModificationLog() {
		return this.modificationLog;
	}

	public void setModificationLog(String modificationLog) {
		this.modificationLog = modificationLog;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(final int pNumber) {
		number = pNumber;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<Dataset> getChildren() {
		return children;
	}

	public void setChildren(final List<Dataset> children) {
		this.children = children;
	}

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

	public Set<Dataset> getIdentificationDatasets() {
		Set<Dataset> idfDS = new HashSet<Dataset>();
		if ((getChildren() == null || getChildren().isEmpty()) && (getType().equals(DatasetType.IDENTIFICATION))) {
			idfDS.add((Dataset) this);
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
