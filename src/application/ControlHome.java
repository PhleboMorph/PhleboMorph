package application;


import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import businesslogic.Project;
import facade.Facade;
import helper.Keyboard;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 *
 */
public class ControlHome {

	/**
	 * Default constructor
	 */
	public ControlHome() {
	}


	@FXML
	private TableView<Project> lastProject;

	@FXML
	private TableColumn<Project, String> projectName;

	@FXML
	private TableColumn<Project, String> projectPath;

	@FXML
	private TableColumn<Project, Date> projectDate;


	@FXML
	void newProject(MouseEvent event) {

		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open File");
		File file = chooser.showSaveDialog(new Stage());
		if (file == null)
			System.out.println("You cancelled the choice");
		else
		{
			this.newProject(file.getAbsolutePath() + ".project");

		}


	}

	@FXML
	void openProject(MouseEvent event) {
		if(this.loadProject())
		{
			moveToDashboard();	
		}
		
	}

	/**
	 * Create a new empty project.
	 * @param projectName
	 */
	public void newProject(String path) {
		Facade.newProject(path);
		this.moveToDashboard();
	}

	/**
	 * Load an existing project
	 * @param project
	 */
	public boolean loadProject() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open project");
		chooser.getExtensionFilters().addAll(
		         new ExtensionFilter("Project Files", "*.project"));
		File file = chooser.showOpenDialog(new Stage());
		if (file == null)
		{
			System.out.println("You cancelled the choice");
			return false;
		}
		else
		{
			Facade.loadProject(file);
			return true;
		}

	}

	public void initialize() {

		ArrayList<Project> listProj = Facade.getHistProject();

		projectName.setCellValueFactory(new PropertyValueFactory<>("name"));
		projectPath.setCellValueFactory(new PropertyValueFactory<>("pathProject"));
		projectDate.setCellValueFactory(new PropertyValueFactory<>("lastSave"));
		projectDate.setSortType(TableColumn.SortType.DESCENDING);

		ObservableList<Project> list = FXCollections.observableArrayList(listProj);
		lastProject.setItems(list);

	}


	@FXML
	public void clickItem(MouseEvent event)
	{
		if(Facade.loadProject(new File (this.lastProject.getSelectionModel().getSelectedItem().getPathProject())))
		{
			this.moveToDashboard();
		}
		else
		{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Unable to open project !");
			alert.showAndWait();
		}

	}

	void moveToDashboard() {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("UIDashboard.fxml"));
			root = loader.load();

			Scene scene = new Scene(root);
			scene.getStylesheets().add("caspian.css");
			scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
				if(key.getCode()==KeyCode.CONTROL && key.getCode()==KeyCode.A)
				{

				}
				else if(key.getCode()==KeyCode.CONTROL) {
					Keyboard.setCtrl();
				}
			});
			
			Stage stage = new Stage();
			stage.setFullScreen(true);
			stage.setScene(scene);
			stage.show();

			ControlDashboard myController = loader.getController();


			//myController.setDataOptions();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	void clearHistoric(ActionEvent event) {
		Facade.clearHistoric();

	}

}