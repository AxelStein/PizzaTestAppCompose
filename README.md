# Pizza Test App (Jetpack Compose)

A sample Android application built with Jetpack Compose demonstrating dynamic data rendering, interactive state management, and modern UI implementation based on Figma designs.

---

## 🎨 Design Reference

* **Figma Project:** [Pizza Mobile App Design](https://www.figma.com/design/2tHiYh4OtUehtadCXmVqtp/TEST--Pizza-Mobile-App--Copy)

### Visual Preview
![Ref](Pepperoni%20Blast%20(M).png)

---

## 🔌 API Specification

The application consumes a dynamic pizza catalog endpoint. Below is the JSON payload structure used to populate the product detail view and handle sizing variants:

```json
{
  "pizzas": [
    {
      "id": "midnight-harvest",
      "name": "Midnight Harvest",
      "description": "This pizza celebrates the rich and bold flavors of black olives paired with a medley of cheeses. The deep, earthy taste of black olives harmonizes beautifully with the creamy, melted cheeses.",
      "image_url": "[https://oursongapp.com/images/pizzas/pizza_midnight_harvest.png](https://oursongapp.com/images/pizzas/pizza_midnight_harvest.png)",
      "variants": [
        {
          "size": "S",
          "price": 14.99
        },
        {
          "size": "M",
          "price": 17.99
        },
        {
          "size": "L",
          "price": 21.99
        }
      ],
      "default_size": "M"
    }
  ]
}
