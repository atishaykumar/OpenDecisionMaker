
/**
 * 
 * @author Bender, Blocherer, Rossmehl and Rotter
 * 
 *         This file is part of Open Decision Maker.
 * 
 *         Open Decision Maker is free software: you can redistribute it and/or
 *         modify it under the terms of the GNU General Public License as
 *         published by the Free Software Foundation, either version 3 of the
 *         License, or (at your option) any later version.
 * 
 *         Open Decision Maker is distributed in the hope that it will be
 *         useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 *         of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *         General Public License for more details.
 * 
 *         You should have received a copy of the GNU General Public License
 *         along with Open Decision Maker. If not, see
 *         <http://www.gnu.org/licenses/>.
 */
package r2b2.odm.reporting;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.eclipse.swt.program.Program;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import r2b2.odm.gui.Main_Gui;
import r2b2.odm.model.AhpModel;

/**
 * Creates and exports an report by using the given jrxml-Template and modifying it according it to the given report content.
 * @author Rossmehl
 */
public class ReportPrinter {

 int bandHeight = 0;
 int parameterCounter = 0;
 double criticalCr = 0.12;

 /**
  * Creates exports and launches the report.
  * 
  * @param model
  * @param criticalCr
  * @param fileName
  */
 public void printReport(AhpModel model, double criticalCr, String fileName) {
  this.criticalCr = criticalCr;

  //create reportContent object by transforming the model data with the ReportContentBuilder 
  ReportContentBuilder reportContentBuilder = new ReportContentBuilder();
  ReportContent reportContent = reportContentBuilder
    .getReportContentFromAhpModel(model);
  
  //create template and export it 
  createTemplate(reportContent, fileName);

  //launch the exported file (with an installed PDF reader)
  Program.launch(fileName);

 }

 /**
  * Modifies the given report_template.jrxml due to the reportContent and exports the filled report to the given file path.
  * 
  * @param reportContent
  * @param fileName
  */
 public void createTemplate(ReportContent reportContent, String fileName) {

  ArrayList<Detail> detailList = reportContent.getDetails();

  // create Test Data
  HashMap<String, Object> parameter = createBasicParameters(reportContent);

  // read and modify file
  
  
  File file = Main_Gui.openFile("report_template.jrxml", "report template", ".jrxml");

  try {
   // define namespace of the jrxml-file and document
   Namespace ns = Namespace
     .getNamespace("http://jasperreports.sourceforge.net/jasperreports");
   Document doc = new SAXBuilder(false).build(file);

   // set Color of the result conistency ratio (red, if Cr is higher than
   // the critical CR --> else: black)
   @SuppressWarnings("unchecked")
   List<Element> list = doc.getRootElement().getChild("detail", ns)
     .getChild("band", ns).getChildren();

   
   for (Element element : list) {

    if (element.getName().equals("textField")
      && element.getChild("textFieldExpression", ns)
        .getText()
        .equals("$P{RESULT_CONSISTENCY_RATIO}")) {

     setConsistencyRatioColor(
       reportContent.getResultConsistencyRatio(), element,
       ns);
    }
   }

   // addDetail band(s)
   for (Detail detail : detailList) {
    this.addDetailFrame(doc, ns, parameter, detail);

   }

   // remove detail pattern band (last band under "detail" in "report_template.jrxml")
   doc.getRootElement().getChild("detail", ns).getChildren().remove(4);

   // create output of the modified template 
   XMLOutputter out = new XMLOutputter();
   ByteArrayOutputStream baos = new ByteArrayOutputStream();
   out.output(doc, baos);

   ByteArrayInputStream bais = new ByteArrayInputStream(
     baos.toByteArray());

   // -----------
   // --JASPER
   // ----------
   
   // compile, fill and export the report as PDF-file to the given path
   JasperReport jasperReport = JasperCompileManager
     .compileReport(bais);

   JasperPrint jasperPrint = JasperFillManager.fillReport(
     jasperReport, parameter,
     new net.sf.jasperreports.engine.JREmptyDataSource(1));

   JasperExportManager.exportReportToPdfFile(jasperPrint, fileName);

  } catch (JRException e) {
   // print out standard exception
   e.printStackTrace();
  } catch (JDOMException e1) {
   // print out standard exception
   e1.printStackTrace();
  } catch (IOException e1) {
   // print out standard exception
   
   //!!NOT YET IMPLEMENTED!!:
   //show user dialog, if file which shall be overwritten is still opened.
   
   e1.printStackTrace();
  }

 }

 /**
  * Returns HashMap which contains all basic parameters of the report. Does not contain detail parameters.
  * 
  * @param reportContent
  * @return HashMap which contains all basic parameters
  */
 
 private HashMap<String, Object> createBasicParameters(
   ReportContent reportContent) {
  HashMap<String, Object> parameter = new HashMap<String, Object>();
  parameter.put("RESULT_ALTERNATIVES",
    reportContent.getResultAlternatives());
  parameter.put("GOAL_NAME", reportContent.getGoalName());
  parameter.put("RESULT_ALTERNATIVE_CRITERION_MATRIX",
    reportContent.getResultAlternativeCriterionMatrix());
  parameter.put("RESULT_CRITERIA", reportContent.getResultCriteria());
  parameter.put("RESULT_CONSISTENCY_RATIO",
    reportContent.getResultConsistencyRatio());
  parameter.put("RESULT_CRITICAL_CONSISTENCY_RATIO",
    Double.toString(this.criticalCr));

  return parameter;
 }

 
 /**
  * Adds a detail band inclusive necessary paramters to a given jrxml-document.
  * 
  * @param doc
  * @param ns
  * @param parameter
  * @param detail
  * @return document
  */
 public Document addDetailFrame(Document doc, Namespace ns,
   HashMap<String, Object> parameter, Detail detail) {

  // add paramters with "_<DetailName>" extension
  String parameterExtension = "_" + detail.getName().toUpperCase();
  this.addParameter(doc, ns, "DETAIL_NAME" + parameterExtension,
    "java.lang.String");
  this.addParameter(doc, ns, "DETAIL_TYPE" + parameterExtension,
    "java.lang.String");
  this.addParameter(doc, ns, "DETAIL_DESCRIPTION" + parameterExtension,
    "java.lang.String");
  this.addParameter(doc, ns, "DETAIL_COMPARISON" + parameterExtension,
    "java.util.ArrayList");
  this.addParameter(doc, ns, "DETAIL_RANKING" + parameterExtension,
    "java.util.ArrayList");
  this.addParameter(doc, ns, "DETAIL_CONSISTENCY_RATIO"
    + parameterExtension, "java.lang.String");
  this.addParameter(doc, ns, "DETAIL_NUMBER" + parameterExtension,
    "java.lang.String");
  this.addParameter(doc, ns, "DETAIL_PARENTS" + parameterExtension,
    "java.lang.String");

  // get pattern band element (at index 4)
  Element detailElement = doc.getRootElement().getChild("detail", ns);
  @SuppressWarnings("unchecked")
  List<Element> detailElementList = detailElement.getChildren();

  Element bandElement = detailElementList.get(4);

  // clone pattern element
  Element newBandElement = (Element) bandElement.clone();
  @SuppressWarnings("unchecked")
  List<Element> bandList = newBandElement.getChildren();

  // get child "frame"
  Element frame = bandList.get(bandList.size() - 1);
  @SuppressWarnings("unchecked")
  List<Element> frameChildren = frame.getChildren();

  // iterate over frame Children
  for (Element thisElement : frameChildren) {

   // find all textfields and replace parameter names
   if (thisElement.getName().equals("textField")) {
    Element textFieldExpression = thisElement.getChild(
      "textFieldExpression", ns);
    String textFieldExpressionText = textFieldExpression.getText();

    
    if (textFieldExpressionText.equals("$P{DETAIL_DESCRIPTION}")) {
     textFieldExpression
       .setContent(new CDATA("$P{DETAIL_DESCRIPTION"
         + parameterExtension + "}"));

    } else if (textFieldExpressionText
      .equals("$P{DETAIL_CONSISTENCY_RATIO}")) {
     textFieldExpression.setContent(new CDATA(
       "$P{DETAIL_CONSISTENCY_RATIO" + parameterExtension
         + "}"));

     // set Color of Conistency Ratio (red, if Cr is higher than
     // the critical CR
     setConsistencyRatioColor(detail.getConsistencyRatio(),
       thisElement, ns);

    } else if (textFieldExpressionText
      .equals("$P{DETAIL_NUMBER} + \" \" +$P{DETAIL_TYPE} + \": \" + $P{DETAIL_NAME}")) {
     textFieldExpression.setContent(new CDATA("$P{DETAIL_NUMBER"
       + parameterExtension + "} + \" \" +$P{DETAIL_TYPE"
       + parameterExtension + "}+ \": \" + $P{DETAIL_NAME"
       + parameterExtension + "}"));

     //set new detail parents parameter name
    } else if (textFieldExpressionText
      .equals("\"Parent(s): \" + $P{DETAIL_PARENTS}")) {
     textFieldExpression.setContent(new CDATA(
       "\"Parent(s): \" + $P{DETAIL_PARENTS"
         + parameterExtension + "}"));

    }

    // modify crosstab parameter expression
   } else if (thisElement.getName().equals("crosstab")) {
    thisElement
      .getChild("crosstabDataset", ns)
      .getChild("dataset", ns)
      .getChild("datasetRun", ns)
      .getChild("dataSourceExpression", ns)
      .setContent(
        new CDATA(
          "new net.sf.jasperreports.engine.data.JRBeanCollectionDataSource($P{DETAIL_COMPARISON"
            + parameterExtension + "})"));

    // modfiy table parameter expression
   } else if (thisElement.getName().equals("componentElement")) {
    Namespace tableNs = Namespace
      .getNamespace("http://jasperreports.sourceforge.net/jasperreports/components");
    thisElement
      .getChild("table", tableNs)
      .getChild("datasetRun", ns)
      .getChild("dataSourceExpression", ns)
      .setContent(
        new CDATA(
          "new net.sf.jasperreports.engine.data.JRBeanCollectionDataSource($P{DETAIL_RANKING"
            + parameterExtension + "})"));
   }

  }

  // add modified band-element to detail-element
  detailElement.addContent(newBandElement);

  // put parameters of the added detail band to the parameter HashMap
  parameter.put("DETAIL_NAME" + parameterExtension, detail.getName());
  parameter.put("DETAIL_TYPE" + parameterExtension, detail.getType());
  parameter.put("DETAIL_DESCRIPTION" + parameterExtension,
    detail.getDescription());
  parameter.put("DETAIL_COMPARISON" + parameterExtension,
    detail.getComparison());
  parameter.put("DETAIL_RANKING" + parameterExtension,
    detail.getRanking());

  parameter.put("DETAIL_CONSISTENCY_RATIO" + parameterExtension,
    detail.getConsistencyRatio());
  parameter.put("DETAIL_NUMBER" + parameterExtension, detail.getNumber());
  parameter.put("DETAIL_PARENTS" + parameterExtension,
    detail.getParents());

  return doc;
 }

 /**
  * Adds a parameter to a document.
  * 
  * @param doc the given jrxml -document
  * @param ns the given namespace
  * @param parameterName name of the parameter to add
  * @param parameterDataType datatype of the paramter to add
  */
 public void addParameter(Document doc, Namespace ns, String parameterName,
   String parameterDataType) {

  // get parameter element
  Element parameterElement = (Element) doc.getRootElement()
    .getChild("parameter", ns).clone();

  // set parameter attributes "name" and "class"
  parameterElement.setAttribute("name", parameterName);
  parameterElement.setAttribute("class", parameterDataType);

  // add element to document
  @SuppressWarnings("unchecked")
  List<Element> children = doc.getRootElement().getChildren();

  int index = 0;

  for (Element element : children) {
   String thisChildName = element.getName();

   // get element over "title"-element
   if (thisChildName == "title") {
    index = doc.getRootElement().indexOf(element) - 1;

   }

  }

  doc.getRootElement().addContent(index, parameterElement);

  parameterCounter++;

 }

 /**
  * Compare a consistency ratio to the critical value for consistency ratios. 
  * If the CR of this element is higher than or equal to the critical CR, the text color ist set to red 
  * 
  * @param consistencyRatio consistency ratio string which shall be displayed within the textfield
  * @param thisElement jrxml text field element 
  * @param ns given namespace
  */
 private void setConsistencyRatioColor(String consistencyRatio,
   Element thisElement, Namespace ns) {
  
  //get double valie of the consistency ratio string
  double thisConsistencyRatio = (new Double(consistencyRatio.replace(',',
    '.'))).doubleValue();
  
  // get critical consistentcy ratio
  double criticalCR = this.criticalCr;

  // compare the two values. 
  // if the CR of this element is higher than or equal to the critical CR, the text color ist set to red 
  if (thisConsistencyRatio >= criticalCR) {
   Element reportElement = thisElement.getChild("reportElement", ns);
   reportElement.setAttribute("forecolor", "#FF3300");

  }

 }

}