package tenx.store.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Order {
	private Long id;
	private BigDecimal cost;
	private List<LineItem> items = new ArrayList<LineItem>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public List<LineItem> getItems() {
		return items;
	}

	public void addItem(LineItem item) {
		items.add(item);
	}

}
