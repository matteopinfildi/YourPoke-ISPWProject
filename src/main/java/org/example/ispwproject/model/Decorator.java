    package org.example.ispwproject.model;

    public class Decorator extends  Ingredient{

        private Ingredient ingredient;

        protected Decorator(Ingredient ingredient){this.ingredient = ingredient;}

        @Override
        public double price(){
            double ingredientPrice = this.ingredient.price();
            return ingredientPrice;
        }
    }
