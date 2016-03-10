package net.javierjimenez.SeriesTV;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javafx.event.ActionEvent;

import javafx.scene.control.ListView;

import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;

public class SeriesController {

	/**
	 * Componentes de nuestro scene
	 */
	@FXML
	private TextField search;
	@FXML
	private ComboBox<String> temporadas = new ComboBox<>();
	@FXML
	private ListView<String> list_cap = new ListView<>();
	@FXML
	private Label cap_cast = new Label();
	@FXML
	private Label cap_lat = new Label();
	@FXML
	private Label cap_eng = new Label();
	@FXML
	private Label cap_vos = new Label();

	/**
	 * Objeto List que contiene WebElements que engloban los h2 que buscamos en
	 * la página
	 */
	private List<WebElement> temp;

	/**
	 * Objeto WebDriver que permite abrir una ventana del navegador que hemos
	 * elegido gracias a sus drivers. En este caso el navegador a usar es
	 * Firefox.
	 */
	private WebDriver navegador;

	/**
	 * Metodo encargado de encontrar las temporadas de una serie. Si hacemos una
	 * busqueda y nos sale mas de una serie o no nos sale ninguna, se abrira un
	 * Dialog y nos pedira que repitamos la busqueda, cerrando la ventana del
	 * navegador.
	 * 
	 * @param event
	 */
	@FXML
	public void busquedaSeries(ActionEvent event) {

		temporadas.setPromptText("Temporadas");

		navegador = new FirefoxDriver();

		navegador.get("http://seriesblanco.com");

		WebElement buscador = navegador.findElement(By.id("buscar-blanco"));
		buscador.sendKeys(search.getText());
		buscador.submit();

		WebElement boxSeries = navegador.findElement(By.cssSelector("div.date-outer div.post-header"));

		WebElement divSeries = boxSeries.findElement(By.cssSelector("div:nth-child(3)"));

		List<WebElement> series = divSeries.findElements(By.tagName("img"));

		if (series.size() > 1 || series.size() == 0) {

			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Aletra: Busqueda Infructuosa");
			alert.setHeaderText("Problema con la busqueda realizada.");
			alert.setContentText("El resultado no es el deseado.\nHaga una nueva.");
			alert.showAndWait();

			navegador.close();

		} else {

			WebElement serie = divSeries.findElement(By.xpath("./div[1]/div/a"));

			serie.click();

			WebDriverWait wait = new WebDriverWait(navegador, 30);

			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(@id, 'post-body-')]")));

			WebElement boxTemp = navegador.findElement(By.xpath("//*[contains(@id, 'post-body-')]"));

			temp = boxTemp.findElements(By.tagName("h2"));

			for (WebElement t : temp) {

				temporadas.getItems().add(t.getText());

			}

			navegador.manage().window().setPosition(new Point(-2000, 0));
		}
	}

	/**
	 * Metodo que se activa al elegir una de las temporadas de la serie. Cuando
	 * haces esto, se muestra un listado con todos los capitulos y cuantos
	 * enlaces en castellano, latino, ingles y VOS hay por cada temporada.
	 * 
	 * @param event
	 */
	@FXML
	public void listaTemporadas(ActionEvent event) {

		int es = 0, lat = 0, eng = 0, vos = 0;

		list_cap.getItems().clear();

		WebElement tabla = navegador
				.findElement(By.xpath("//u[text() = '" + temporadas.getValue() + "' ]/../following-sibling::table"));

		List<WebElement> listaCapitulos = tabla.findElements(By.xpath("./tbody/tr/td[1]/a"));

		List<WebElement> idiomas = tabla.findElements(By.xpath("./tbody/tr/td[2]/img"));

		for (WebElement cap : listaCapitulos) {

			String hrefCap = cap.getAttribute("href");
			
			String [] nomCap = hrefCap.split("/");
			
			list_cap.getItems().add(nomCap[nomCap.length - 2]);

		}

		for (WebElement idioma : idiomas) {

			String idiom = idioma.getAttribute("src");

			if (idiom.contains("es.png")) {
				es++;
			} else if (idiom.contains("la.png")) {
				lat++;
			} else if (idiom.contains("vos.png")) {
				vos++;
			} else {
				eng++;
			}

		}

		cap_cast.setText(String.valueOf(es));
		cap_lat.setText(String.valueOf(lat));
		cap_eng.setText(String.valueOf(eng));
		cap_vos.setText(String.valueOf(vos));

		System.out.println(list_cap.getItems().size() + " capítulos");
	}
}
