package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.CoppiaPorzioni;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	/*public List<Food> listAllFoods(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}*/
	
	public List<String> getNomiPorzioniMaxCalorie(Double calorie){
		String sql = "SELECT DISTINCT portion_display_name AS n FROM porzioni WHERE calories<?";
		List<String> list = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, calorie);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				list.add(res.getString("n").toLowerCase());
			} 
			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<CoppiaPorzioni> listCoppiePorzioni(Double calorie){
		String sql = "SELECT DISTINCT p1.portion_display_name AS n1, p2.portion_display_name AS n2, COUNT(p1.food_code) AS c " +				
					"FROM porzioni AS p1, porzioni AS p2 " +										//NELLA SOL AGGUNGE DISTINCT p1.food_code
					"WHERE p1.calories<? AND p2.calories<? " +
					"AND p1.food_code=p2.food_code AND p1.portion_display_name>p2.portion_display_name " +
					"GROUP BY p1.portion_display_name, p2.portion_display_name";
		List<CoppiaPorzioni> list = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, calorie);
			st.setDouble(2, calorie);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				list.add(new CoppiaPorzioni(res.getString("n1").toLowerCase(), res.getString("n2").toLowerCase(), res.getInt("c")));
			} 
			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}

}
