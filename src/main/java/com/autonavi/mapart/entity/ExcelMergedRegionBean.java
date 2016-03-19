package com.autonavi.mapart.entity;

/**
 * excel 合并单元格区域实体类(用于读取带有合并单元格的excel)
 * @author huandi.yang
 *
 */
public class ExcelMergedRegionBean implements Comparable<ExcelMergedRegionBean> {

	private int firstRow;
	private int lastRow;
	private int firstCell;
	private int lastCell;

	public int getFirstCell() {
		return firstCell;
	}

	public void setFirstCell(int firstCell) {
		this.firstCell = firstCell;
	}

	public int getFirstRow() {
		return firstRow;
	}

	public void setFirstRow(int firstRow) {
		this.firstRow = firstRow;
	}

	public int getLastCell() {
		return lastCell;
	}

	public void setLastCell(int lastCell) {
		this.lastCell = lastCell;
	}

	public int getLastRow() {
		return lastRow;
	}

	public void setLastRow(int lastRow) {
		this.lastRow = lastRow;
	}

	public int compareTo(ExcelMergedRegionBean o) {
		if (this.firstRow < o.firstRow) {
			return -1;
		} else if (this.firstRow > o.firstRow) {
			return 1;
		}
		return 0;
	}

	@Override
	public String toString() {
		return "【firstRow" + firstRow + ", lastRow:" + lastRow + ", firstCell:"
				+ firstCell + ", lastCell:" + lastCell + "】\n";
	}
}