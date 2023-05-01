
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;

import javafx.util.Callback;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DBGUI extends Application {

	// 추가 코드
	MetaData md = new MetaData();

	// data members
	private Connection con = MyConnection.mackConnection();
	private TableView table;
	TreeView<String> tree;
	Button[] buttons;
	Label[] labels;
	TextField[] txt;
	TextArea txtArea;
	private final String[] btntext = { "clear", "save", "update", "delete", "print", "search" };

	// function members
	private HBox addCenterPane() {

		HBox hb1 = new HBox();

		// Add TableView
		VBox vb = new VBox();

		table = new TableView<>();
		table.setMinSize(700, 150);
		table.setMaxSize(700, 150);
		table.setStyle("-fx-border-color: Black;");
		table.prefWidthProperty().bind(vb.prefWidthProperty());
		table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		table.getSelectionModel().setCellSelectionEnabled(false);

		// 추가 코드
		table.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
			if (ov != nv) {
				showFields();
			}
		});

		// Add Labels and TextFields
		GridPane gp = new GridPane();
		gp.setPadding(new Insets(15, 15, 15, 125));
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setStyle("-fx-border-color: Blue;");
		gp.prefHeightProperty().bind(table.prefWidthProperty());
		txt = new TextField[10];
		labels = new Label[10];

		for (int i = 0; i < labels.length; i++) {
			labels[i] = new Label("Label..");
			labels[i].setMinSize(150, 25);

			txt[i] = new TextField(" Text.. ");
			txt[i].setMinSize(300, 20);

			gp.addRow(i, labels[i], txt[i]);

			labels[i].prefHeightProperty().bind(gp.widthProperty());
			txt[i].prefHeightProperty().bind(gp.widthProperty());
		}

		vb.getChildren().addAll(table, gp);

		// Add TreeView
		StackPane stack = new StackPane();

		// Create the tree

		tree = addNodestoTree();
		tree.setShowRoot(true);

		tree.setMaxWidth(150);
		tree.prefWidthProperty().bind(stack.prefWidthProperty());

		// 추가 코드
		// 버튼을 누른 것에 따라 시행되는 것을 정의?
		tree.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {

			if (nv != ov) {
				// 어떤 것을 눌렀는지 텍스트로 반환
				String str = mySelectedNode();

				if (str.equals(Nodes.Paper.toString()) || str.equals(Nodes.Professor.toString())
						|| str.equals(Nodes.Research.toString()) || str.equals(Nodes.Student.toString())
						|| str.equals(Nodes.Subject.toString()) || str.equals(Nodes.Take.toString())) {

					rsToTableView(str);
				}

				// 추가 코드
				else if (str.equals(Nodes.About.toString())) {
					md.getStage();
				}
			}
		});

		stack.getChildren().add(tree);

		hb1.getChildren().addAll(stack, vb);
		hb1.setStyle("-fx-border-color:black;");
		hb1.setSpacing(20);
		hb1.prefHeightProperty().bind(vb.prefWidthProperty());

		return hb1;
	}

	// 추가 코드
	// 어떤 것을 눌렀는지 텍스트로 반환
	private String mySelectedNode() {
		// return
		// tree.getSelectionModel().selectedItemProperty().getValue().getValue().toString();

		TreeItem ti = tree.getSelectionModel().selectedItemProperty().getValue();
		return ti.getValue().toString();
	}

	private StackPane addBottomPane() {

		StackPane stack = new StackPane();

		stack.setStyle("-fx-border-color: #336699;");

		txtArea = new TextArea();
		txtArea.setMaxHeight(200);
		txtArea.prefHeightProperty().bind(stack.prefWidthProperty());

		stack.getChildren().add(txtArea);

		return stack;
	}

	private HBox addTopPane() {

		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 15, 15, 15));
		hbox.setSpacing(10); // Gap between nodes
		// hbox.setStyle("-fx-background-color: #336699;");
		hbox.setStyle("-fx-border-color: Blue;");

		buttons = new Button[6];
		for (int i = 0; i < buttons.length; i++) {

			buttons[i] = new Button(btntext[i]);
			buttons[i].setPrefSize(80, 20);
			buttons[i].prefHeightProperty().bind(hbox.prefWidthProperty());
		}

		// 추가 코드
		// 버튼에 따라 다른 동작을 정의
		for (int i = 0; i < buttons.length; i++) {
			final int j = i;

			buttons[j].setOnAction(e -> {
				String str = buttons[j].getText();
				txtArea.appendText("You have selected " + str + "\n");

				if ("clear".equals(str)) {
					clearTextFields();

				}

				else if ("save".equals(str)) {
					insertCall();
				} 
				
				else if ("update".equals(str)) {
					updateCall();
				} 
				
				else if ("delete".equals(str)) {
					deleteCall();
				} 
				
				else if ("print".equals(str)) {
					printCall();
				} 

				else if ("search".equals(str)) {

				}

				else {
					txtArea.appendText("Not an approperiate button");
				}
			});
		}

		hbox.getChildren().addAll(buttons);

		return hbox;
	}

	private TreeView<String> addNodestoTree() {
		TreeView<String> tree = new TreeView<String>();

		TreeItem<String> root, tables, reports, exit, about;

		root = new TreeItem<String>("Project");

		tables = new TreeItem<String>("Tables");

		makeChild(Nodes.Paper.toString(), tables);
		makeChild(Nodes.Professor.toString(), tables);
		makeChild(Nodes.Research.toString(), tables);
		makeChild(Nodes.Student.toString(), tables);
		makeChild(Nodes.Subject.toString(), tables);
		makeChild(Nodes.Take.toString(), tables);

		reports = new TreeItem<String>("Reports");
		makeChild(Nodes.Report01.toString(), reports);
		makeChild(Nodes.Report02.toString(), reports);
		makeChild(Nodes.Report03.toString(), reports);

		exit = new TreeItem<String>(Nodes.Exit.toString());
		about = new TreeItem<String>(Nodes.About.toString());
		root.getChildren().addAll(tables, reports, exit, about);
		tree.setRoot(root);

		return tree;

	}

	// Create child
	private TreeItem<String> makeChild(String title, TreeItem<String> parent) {
		TreeItem<String> item = new TreeItem<>(title);
		item.setExpanded(false);
		parent.getChildren().add(item);
		return item;
	}

	@Override
	public void start(Stage stage) {

		// Add controls and Layouts

		VBox vbox = new VBox();
		vbox.setSpacing(20);
		vbox.setMinSize(900, 500);
		vbox.setMaxSize(1200, 700);
		vbox.setPadding(new Insets(15, 15, 15, 15));
		vbox.setSpacing(10); // Gap between nodes
		vbox.setStyle("-fx-border-color: Black;");

		// Top Box
		HBox tbox = addTopPane();
		tbox.prefHeightProperty().bind(vbox.prefWidthProperty());
		vbox.getChildren().add(tbox);

		// Center box
		HBox cbox = addCenterPane();
		cbox.prefHeightProperty().bind(vbox.prefWidthProperty());
		vbox.getChildren().add(cbox);

		// 변경 코드
		StackPane bbox = addBottomPane();
		bbox.prefHeightProperty().bind(vbox.prefWidthProperty());
		vbox.getChildren().add(bbox);

		// create and show stage
		Scene scene = new Scene(vbox);
		stage.setScene(scene);
		stage.setTitle("Project");
		stage.show();
	}

	private void rsToTableView(String s) {

		table.getColumns().clear();

		for (int i = 0; i < table.getItems().size(); i++) {
			table.getItems().clear();
		}

		ObservableList data = FXCollections.observableArrayList();

		try {
			String query = "select * from " + s + "";

			PreparedStatement pst = null;
			pst = con.prepareStatement(query);

			ResultSet rs = pst.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();

			int colCount = rsmd.getColumnCount();

			// get data rows

			for (int i = 0; i < colCount; i++) {

				int dataType = rsmd.getColumnType(i + 1);

				final int j = i;

				TableColumn col = new TableColumn(rsmd.getColumnName(i + 1));

				col.setCellValueFactory(
						new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
							public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
								return new SimpleStringProperty(param.getValue().get(j).toString());
							}
						});

				table.getColumns().addAll(col);
			}

			while (rs.next()) {

				ObservableList<String> row = FXCollections.observableArrayList();

				for (int k = 1; k <= colCount; k++) {
					String str1 = rs.getString(k);

					if (str1 == null)
						str1 = "null";

					row.add(str1);
				}

				data.add(row);
			}

			table.setItems(data);

			table.getSelectionModel().select(0);

			showFields();

		} catch (Exception e) {
			txtArea.appendText(e.getMessage());

		} finally {
		}
	}

	private void showFields() {

		clearFields();

		// 행 번호 (테이블의 행 번호)
		int row = 0;

		TablePosition pos = (TablePosition) table.getSelectionModel().getSelectedCells().get(0);

		System.out.println(table.getSelectionModel().getSelectedItem());

		row = pos.getRow();

		int cols = table.getColumns().size();

		for (int j = 0; j < cols; j++) {

			Object ch = ((TableColumnBase) table.getColumns().get(j)).getText();

			Object cell = ((TableColumnBase) table.getColumns().get(j)).getCellData(row).toString();

			if (cell == null) {

				txt[j].setText("");

			} else {

				txt[j].setText(cell.toString());
				txt[j].setVisible(true);
			}

			labels[j].setText(ch.toString());
			labels[j].setVisible(true);
		}
	}

	private void clearFields() {
		for (int i = 0; i < txt.length; i++) {
			txt[i].setText("");
			txt[i].setVisible(false);

			labels[i].setText("");
			labels[i].setVisible(false);
		}
	}

	private void clearTextFields() {
		int noc = table.getColumns().size();

		for (int i = 0; i < noc; i++) {
			txt[i].setText(" ");
		}
	}

	// 테이블 별 데이터 추가 함수 호출
	private void insertCall() {
		String str = mySelectedNode();

		if (str.equals(Nodes.Paper.toString())) {
			addPaperSP();
		}

		else if (str.equals(Nodes.Professor.toString())) {
			addProfessorSP();
		}

		else if (str.equals(Nodes.Research.toString())) {
			addResearchSP();
		}

		else if (str.equals(Nodes.Student.toString())) {
			addStudentSP();
		}

		else if (str.equals(Nodes.Subject.toString())) {
			addSubjectSP();
		}

		else if (str.equals(Nodes.Take.toString())) {
			addTakeSP();
		}
	}

	
	private void addPaperSP() {

		String sql = "{call sp_insertPaper(?, ?, ?, ?)}";

		try {
			CallableStatement cst = con.prepareCall(sql);

			cst.setString(1, txt[0].getText());
			cst.setString(2, txt[1].getText());
			cst.setString(3, txt[2].getText());
			cst.setString(4, txt[3].getText());
			cst.execute();

			txtArea.appendText("record is added...");
			cst.close();

		} catch (Exception e) {
			txtArea.appendText(e.getMessage() + "\n");

		} finally {

		}
	}
	
	private void addProfessorSP() {

		String sql = "{call sp_insertProfessor(?, ?, ?, ?)}";

		try {
			CallableStatement cst = con.prepareCall(sql);

			cst.setString(1, txt[0].getText());
			cst.setString(2, txt[1].getText());
			cst.setString(3, txt[2].getText());
			cst.setString(4, txt[3].getText());
			cst.execute();

			txtArea.appendText("record is added...");
			cst.close();

		} catch (Exception e) {
			txtArea.appendText(e.getMessage() + "\n");

		} finally {

		}
	}
	
	private void addResearchSP() {

		String sql = "{call sp_insertResearch(?, ?, ?, ?)}";

		try {
			CallableStatement cst = con.prepareCall(sql);

			cst.setString(1, txt[0].getText());
			cst.setString(2, txt[1].getText());
			cst.setString(3, txt[2].getText());
			cst.setString(4, txt[3].getText());
			cst.execute();

			txtArea.appendText("record is added...");
			cst.close();

		} catch (Exception e) {
			txtArea.appendText(e.getMessage() + "\n");

		} finally {

		}
	}
	
	private void addStudentSP() {

		String sql = "{call sp_insertStudent(?, ?, ?, ?, ?)}";

		try {
			CallableStatement cst = con.prepareCall(sql);

			cst.setString(1, txt[0].getText());
			cst.setString(2, txt[1].getText());
			cst.setString(3, txt[2].getText());
			cst.setString(4, txt[3].getText());
			cst.setString(5, txt[4].getText());
			cst.execute();

			txtArea.appendText("record is added...");
			cst.close();

		} catch (Exception e) {
			txtArea.appendText(e.getMessage() + "\n");

		} finally {

		}
	}
	
	private void addSubjectSP() {

		String sql = "{call sp_insertSubject(?, ?, ?, ?, ?, ?)}";

		try {
			CallableStatement cst = con.prepareCall(sql);

			cst.setString(1, txt[0].getText());
			cst.setString(2, txt[1].getText());
			cst.setString(3, txt[2].getText());
			cst.setString(4, txt[3].getText());
			cst.setString(5, txt[4].getText());
			cst.setString(6, txt[5].getText());
			cst.execute();

			txtArea.appendText("record is added...");
			cst.close();

		} catch (Exception e) {
			txtArea.appendText(e.getMessage() + "\n");

		} finally {

		}
	}
	
	private void addTakeSP() {

		String sql = "{call sp_insertTake(?, ?, ?)}";

		try {
			CallableStatement cst = con.prepareCall(sql);

			cst.setString(1, txt[0].getText());
			cst.setString(2, txt[1].getText());
			cst.setString(3, txt[2].getText());
			cst.execute();

			txtArea.appendText("record is added...");
			cst.close();

		} catch (Exception e) {
			txtArea.appendText(e.getMessage() + "\n");

		} finally {

		}
	}
	
	

	
	
	
	// 테이블 별 삭제 프로시저 호출
	private void deleteCall() {
		String str = mySelectedNode();

		if (str.equals(Nodes.Paper.toString())) {
			deletePaperSP();
		}

		else if (str.equals(Nodes.Professor.toString())) {
			deleteProfessorSP();
		}

		else if (str.equals(Nodes.Research.toString())) {
			deleteResearchSP();
		}

		else if (str.equals(Nodes.Student.toString())) {
			deleteStudentSP();
		}

		else if (str.equals(Nodes.Subject.toString())) {
			deleteSubjectSP();
		}

		else if (str.equals(Nodes.Take.toString())) {
			deleteTakeSP();
		}
	}
	

	private void deletePaperSP() {
		try {
			String sql = "{call sp_deletePaper(?)}";

			CallableStatement cst = con.prepareCall(sql);

			cst.setString(1, txt[0].getText());
			cst.execute();

			txtArea.appendText("Record is deleted...\n");
			cst.close();

		} catch (Exception e) {
			txtArea.appendText(e.getMessage() + "\n");

		} finally {

		}
	}
	
	private void deleteProfessorSP() {
		try {
			String sql = "{call sp_deleteProfessor(?)}";

			CallableStatement cst = con.prepareCall(sql);

			cst.setString(1, txt[0].getText());
			cst.execute();

			txtArea.appendText("Record is deleted...\n");
			cst.close();

		} catch (Exception e) {
			txtArea.appendText(e.getMessage() + "\n");

		} finally {

		}
	}
	
	private void deleteResearchSP() {
		try {
			String sql = "{call sp_deleteResearch(?, ?)}";

			CallableStatement cst = con.prepareCall(sql);

			cst.setString(1, txt[0].getText());
			cst.setString(2, txt[1].getText());
			cst.execute();

			txtArea.appendText("Record is deleted...\n");
			cst.close();

		} catch (Exception e) {
			txtArea.appendText(e.getMessage() + "\n");

		} finally {

		}
	}
	
	private void deleteStudentSP() {
		try {
			String sql = "{call sp_deleteStudent(?)}";

			CallableStatement cst = con.prepareCall(sql);

			cst.setString(1, txt[0].getText());
			cst.execute();

			txtArea.appendText("Record is deleted...\n");
			cst.close();

		} catch (Exception e) {
			txtArea.appendText(e.getMessage() + "\n");

		} finally {

		}
	}
	
	private void deleteSubjectSP() {
		try {
			String sql = "{call sp_deleteSubject(?, ?)}";

			CallableStatement cst = con.prepareCall(sql);

			cst.setString(1, txt[0].getText());
			cst.setString(2, txt[3].getText());
			cst.execute();

			txtArea.appendText("Record is deleted...\n");
			cst.close();

		} catch (Exception e) {
			txtArea.appendText(e.getMessage() + "\n");

		} finally {

		}
	}
	
	private void deleteTakeSP() {
		try {
			String sql = "{call sp_deleteTake(?, ?)}";

			CallableStatement cst = con.prepareCall(sql);

			cst.setString(1, txt[0].getText());
			cst.setString(2, txt[1].getText());
			cst.execute();

			txtArea.appendText("Record is deleted...\n");
			cst.close();

		} catch (Exception e) {
			txtArea.appendText(e.getMessage() + "\n");

		} finally {

		}
	}

	
	
	
//	// 테이블 별 update 프로시저 호출
	private void updateCall() {
		String str = mySelectedNode();

		if (str.equals(Nodes.Paper.toString())) {
			updatePaperSP();
		}

		else if (str.equals(Nodes.Professor.toString())) {
			updateProfessorSP();
		}

		else if (str.equals(Nodes.Research.toString())) {
			updateResearchSP();
		}

		else if (str.equals(Nodes.Student.toString())) {
			updateStudentSP();
		}

		else if (str.equals(Nodes.Subject.toString())) {
			updateSubjectSP();
		}

		else if (str.equals(Nodes.Take.toString())) {
			updateTakeSP();
		}
	}
	

	private void updatePaperSP() {

		String sql = "{call sp_updatePaper(?, ?, ?, ?)}";

		try {
			CallableStatement cst = con.prepareCall(sql);

			cst.setString(1, txt[0].getText());
			cst.setString(2, txt[1].getText());
			cst.setString(3, txt[2].getText());
			cst.setString(4, txt[3].getText());
			cst.execute();

			txtArea.appendText("record is updated...");
			cst.close();

		} catch (Exception e) {
			txtArea.appendText(e.getMessage() + "\n");

		} finally {

		}
	}
	
	private void updateProfessorSP() {

		String sql = "{call sp_updateProfessor(?, ?, ?, ?)}";

		try {
			CallableStatement cst = con.prepareCall(sql);

			cst.setString(1, txt[0].getText());
			cst.setString(2, txt[1].getText());
			cst.setString(3, txt[2].getText());
			cst.setString(4, txt[3].getText());
			cst.execute();

			txtArea.appendText("record is updated...");
			cst.close();

		} catch (Exception e) {
			txtArea.appendText(e.getMessage() + "\n");

		} finally {

		}
	}
	
	private void updateResearchSP() {

		String sql = "{call sp_updateResearch(?, ?, ?, ?)}";

		try {
			CallableStatement cst = con.prepareCall(sql);

			cst.setString(1, txt[0].getText());
			cst.setString(2, txt[1].getText());
			cst.setString(3, txt[2].getText());
			cst.setString(4, txt[3].getText());
			cst.execute();

			txtArea.appendText("record is updated...");
			cst.close();

		} catch (Exception e) {
			txtArea.appendText(e.getMessage() + "\n");

		} finally {

		}
	}
	
	private void updateStudentSP() {

		String sql = "{call sp_updateStudent(?, ?, ?, ?, ?)}";

		try {
			CallableStatement cst = con.prepareCall(sql);

			cst.setString(1, txt[0].getText());
			cst.setString(2, txt[1].getText());
			cst.setString(3, txt[2].getText());
			cst.setString(4, txt[3].getText());
			cst.setString(5, txt[4].getText());
			cst.execute();

			txtArea.appendText("record is updated...");
			cst.close();

		} catch (Exception e) {
			txtArea.appendText(e.getMessage() + "\n");

		} finally {

		}
	}
	
	private void updateSubjectSP() {

		String sql = "{call sp_updateSubject(?, ?, ?, ?, ?, ?)}";

		try {
			CallableStatement cst = con.prepareCall(sql);

			cst.setString(1, txt[0].getText());
			cst.setString(2, txt[1].getText());
			cst.setString(3, txt[2].getText());
			cst.setString(4, txt[3].getText());
			cst.setString(5, txt[4].getText());
			cst.setString(6, txt[5].getText());
			cst.execute();

			txtArea.appendText("record is updated...");
			cst.close();

		} catch (Exception e) {
			txtArea.appendText(e.getMessage() + "\n");

		} finally {

		}
	}
	
	private void updateTakeSP() {

		String sql = "{call sp_updateTake(?, ?, ?)}";

		try {
			CallableStatement cst = con.prepareCall(sql);

			cst.setString(1, txt[0].getText());
			cst.setString(2, txt[1].getText());
			cst.setString(3, txt[2].getText());
			cst.execute();

			txtArea.appendText("record is updated...");
			cst.close();

		} catch (Exception e) {
			txtArea.appendText(e.getMessage() + "\n");

		} finally {

		}
	}
	


	private void printCall() {
		String str = mySelectedNode();

		if (str.equals(Nodes.Paper.toString())) {
			printPaper();
		}

		else if (str.equals(Nodes.Professor.toString())) {
			printProfessor();
		}

		else if (str.equals(Nodes.Research.toString())) {
			printResearch();
		}

		else if (str.equals(Nodes.Student.toString())) {
			printStudent();
		}

		else if (str.equals(Nodes.Subject.toString())) {
			printSubject();
		}

		else if (str.equals(Nodes.Take.toString())) {
			printTake();
		}
	}
	
	
	
	private void printPaper() {
		try {
			String rfile = "rptPaper.jrxml";

			JasperReport jr = JasperCompileManager.compileReport(rfile);
			
			JasperPrint jp = JasperFillManager.fillReport(jr, null, con);
			JasperViewer.viewReport(jp, false);
			
		} catch (Exception e) {
			txtArea.appendText(e.getMessage() + "\n");

		} finally {

		}
	}
	
	private void printProfessor() {
		try {
			String rfile = "rptProfessor.jrxml";

			JasperReport jr = JasperCompileManager.compileReport(rfile);
			
			JasperPrint jp = JasperFillManager.fillReport(jr, null, con);
			JasperViewer.viewReport(jp, false);
			
		} catch (Exception e) {
			txtArea.appendText(e.getMessage() + "\n");

		} finally {

		}
	}
	
	private void printResearch() {
		try {
			String rfile = "rptResearchs.jrxml";

			JasperReport jr = JasperCompileManager.compileReport(rfile);
			
			JasperPrint jp = JasperFillManager.fillReport(jr, null, con);
			JasperViewer.viewReport(jp, false);
			
		} catch (Exception e) {
			txtArea.appendText(e.getMessage() + "\n");

		} finally {

		}
	}
	
	private void printStudent() {
		try {
			String rfile = "rptStudents.jrxml";

			JasperReport jr = JasperCompileManager.compileReport(rfile);
			
			JasperPrint jp = JasperFillManager.fillReport(jr, null, con);
			JasperViewer.viewReport(jp, false);
			
		} catch (Exception e) {
			txtArea.appendText(e.getMessage() + "\n");

		} finally {

		}
	}
	
	private void printSubject() {
		try {
			String rfile = "rptSubject.jrxml";

			JasperReport jr = JasperCompileManager.compileReport(rfile);
			
			JasperPrint jp = JasperFillManager.fillReport(jr, null, con);
			JasperViewer.viewReport(jp, false);
			
		} catch (Exception e) {
			txtArea.appendText(e.getMessage() + "\n");

		} finally {

		}
	}
	
	private void printTake() {
		try {
			String rfile = "rptTakes.jrxml";

			JasperReport jr = JasperCompileManager.compileReport(rfile);
			
			JasperPrint jp = JasperFillManager.fillReport(jr, null, con);
			JasperViewer.viewReport(jp, false);
			
		} catch (Exception e) {
			txtArea.appendText(e.getMessage() + "\n");

		} finally {

		}
	}
	
	
	

	public static void main(String[] args) {
		launch(args);
	}

}
