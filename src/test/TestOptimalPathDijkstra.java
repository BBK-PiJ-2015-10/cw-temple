package test;

import java.util.Random;

import org.junit.Test;
import org.junit.Before;

//import static org.junit.Assert.*;
//import static org.hamcrest.CoreMatchers.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;



import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

//import cwtwo.colors.Black;


public class TestOptimalPathDijkstra {
	

	
	
	@Before
	public void setup(){
	}
	
	@Test
	public void first(){
		assertEquals(2,2);
		assertThat(2, not(equalTo(3)));
	}
	
	

}
