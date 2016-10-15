package mycash.manager;

import java.util.Arrays;

public class PageCreater {
	private int pageCount;

	public PageCreater() {
		this(5);
	}

	public PageCreater(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int count) {
		pageCount = count;
	}

	public <T> T[] getPage(T[] args, int page) {
		return Arrays.copyOfRange(args, page * getPageCount() - 5, page * getPageCount());
	}
}
