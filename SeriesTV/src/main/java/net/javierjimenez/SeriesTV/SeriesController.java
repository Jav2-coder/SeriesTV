package net.javierjimenez.SeriesTV;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.ListView;

import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;

public class SeriesController {
	@FXML
	private TextField search;
	@FXML
	private ComboBox temporadas;
	@FXML
	private ListView list_cap;
	@FXML
	private Label cap_cast;
	@FXML
	private Label cap_lat;
	@FXML
	private Label cap_eng;
	@FXML
	private Label cap_vos;

	private List<WebElement> temp;
	
	@FXML
	public void busquedaSeries(ActionEvent event) {
		
		WebDriver navegador = new FirefoxDriver();
		navegador.get("http://seriesblanco.com");
		navegador.manage().window().maximize();
		
		WebElement buscador = navegador.findElement(By.id("buscar-blanco"));
		buscador.sendKeys(search.getText());
		buscador.submit();
		
		WebElement boxSeries = navegador.findElement(By.cssSelector("div.date-outer div.post-header"));
		
		WebElement divSeries = boxSeries.findElement(By.cssSelector("div:nth-child(3)"));
		
		List<WebElement> series = divSeries.findElements(By.tagName("img"));
		
		if(series.size() > 1 || series.size() == 0) {
			
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Aletra: Busqueda Infructuosa");
			alert.setHeaderText("Problema con la busqueda realizada.");
			alert.setContentText("El resultado no es el deseado.\nHaga una nueva.");
			alert.showAndWait();
			
			navegador.close();
			
		} else {
			
			WebElement serie = divSeries.findElement(By.xpath("./div[1]/div/a"));
			
			serie.click();
			
			WebDriverWait wait = new WebDriverWait(navegador, 500);
			
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(@id, 'post-body-')]")));
			
			WebElement boxTemp = navegador.findElement(By.xpath("//*[contains(@id, 'post-body-')]"));
			
			temp = boxTemp.findElements(By.tagName("h2"));
			
			for(WebElement t : temp){
				
				temporadas.getItems().add(t.getText());
				
			}
		}
	}
	
	@FXML
	public void listaTemporadas(ActionEvent event) {
		
	}
}
