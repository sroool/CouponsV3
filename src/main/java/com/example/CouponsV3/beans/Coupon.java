/**
 * 
 */
package com.example.CouponsV3.beans;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Table;

/**
 * Coupon bean class
 * 
 * This bean class is used to represent a single coupon that can be added or retrieved from the
 * database.
 *
 */
@Entity
@Table(name="coupons")
public class Coupon {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; // The id of the coupon, generated by the database.
	
	private int companyId; // The id of the company that owns the coupon
	private Category category; // The coupon category
	private String title; // The coupon title
	private String description; // A short description of the coupon
	private Date startDate; // The start date of the coupon
	private Date endDate; // The expiry end date of the coupon
	private int originalAmount; // The amount of coupons of this coupon
	private int currentAmount;
	private double price; // The price of the coupon
	private String image; // The path containing an image of the coupon
	
	/**
	 * 
	 */
	public Coupon() {
		super();
	}
	/**
	 * Coupon CTOR
	 * This constructor will be used to create a new coupon which later can be saved into a database.
	 * params note: 
	 * id is generated by the database only when a coupon is inserted.
	 * 
	 * @param companyId The id of the company that owns the coupon
	 * @param category The coupon category
	 * @param title The coupon title
	 * @param description A short description of the coupon
	 * @param startDate The start date of the coupon
	 * @param endDate The expiry end date of the coupon
	 * @param originalAmount the amount of coupons originally created
	 * @param currentAmount the current amount of coupons
	 * @param price The price of the coupon
	 * @param image The path containing an image of the coupon
	 */
	public Coupon(int companyId, Category category, String title, String description, Date startDate, Date endDate,
			int originalAmount, double price, String image) {
		super();
		this.companyId = companyId;
		this.category = category;
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.originalAmount = originalAmount;
		this.currentAmount = originalAmount;
		this.price = price;
		this.image = image;
	}
	
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @return the companyId
	 */
	public int getCompanyId() {
		return companyId;
	}
	/**
	 * @return the category
	 */
	public Category getCategory() {
		Category.values();
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}
	/**
	 * @param image the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}
	/**
	 * @return the orgininalAmount
	 */
	public int getOriginalAmount() {
		return originalAmount;
	}
	/**
	 * @param orgininalAmount the orgininalAmount to set
	 */
	public void setOriginalAmount(int originalAmount) {
		this.originalAmount = originalAmount;
	}
	/**
	 * @return the currentAmount
	 */
	public int getCurrentAmount() {
		return currentAmount;
	}
	/**
	 * @param currentAmount the currentAmount to set
	 */
	public void setCurrentAmount(int currentAmount) {
		this.currentAmount = currentAmount;
	}
	public void reduceCurrentAmount() {
		this.currentAmount = ( currentAmount - 1);
	}
	@Override
	public String toString() {
		return "Coupon [id=" + id + ", companyId=" + companyId + ", category=" + category + ", title=" + title
				+ ", description=" + description + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", orgininalAmount=" + originalAmount + ", currentAmount=" + currentAmount + ", price=" + price
				+ ", image=" + image + "]";
	}
	
	
	
	
}
