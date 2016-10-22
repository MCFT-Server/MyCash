package mycash.manager;

import java.util.Arrays;
import java.util.List;

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
		return Arrays.copyOfRange(args, page * getPageCount() - getPageCount(), page * getPageCount());
	}
	
	public <T> List<T> getPage(List<T> args, int page) {
		return args.subList(page < 1 ? 0 : page * getPageCount() - getPageCount(), args.size() < page * getPageCount() ? args.size() : page * getPageCount());
	}
}
