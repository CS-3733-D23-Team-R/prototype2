package edu.wpi.teamR.fields;

public class MealFields extends FormFields {
  private String meal;

  public MealFields(String name, String location, String notes, String meal) {
    super(name, location, notes);
    this.meal = meal;
  }

  public MealFields(String name, String location, String meal) {
    super(name, location);
    this.meal = meal;
  }

  public String getMeal() {
    return meal;
  }
}
