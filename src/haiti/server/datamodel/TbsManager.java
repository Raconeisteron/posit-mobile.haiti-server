package haiti.server.datamodel;


import haiti.server.datamodel.AttributeManager.BeneficiaryCategory;
import haiti.server.datamodel.AttributeManager.BeneficiaryType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;

//import com.sun.tools.javac.util.Log;

public class TbsManager {
	public static final String TAG = "TbsManager";
	public static final String RESULT_OK = "Ok";
	private Connection conn;
	private LocaleManager mLocaleManager;
	private String user;
	private String password;
	
	ResultSet result;
	Connection con;
	int resultInt = 0;
	
	

	// public TbsManager() {}

	public TbsManager() {
		readConfigFile();
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			conn = DriverManager.getConnection("jdbc:odbc:acdi_source", user,
					password);
		} catch (ClassNotFoundException f) {
			System.out.println("ClassNotFound Exception");
			f.printStackTrace();
		} catch (SQLException f) {
			System.out.println("SQL Exception");
			f.printStackTrace();
		} catch (Exception f) {
			System.out.println("Exception");
			f.printStackTrace();
		}
		mLocaleManager = new LocaleManager();
	}

	public void readConfigFile() {
		Properties prop = new Properties();
	    String fileName = "config.txt";
	    InputStream is;
		try {
			is = new FileInputStream(fileName);
			prop.load(is);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	    user=prop.getProperty("user");
	    password=prop.getProperty("password");


	}
	/**
	 * METHODE POUR FAIRE DES REQUETES
	 * 
	 * @param chaine
	 *            is the connection to the Db
	 * @param type
	 *            of update
	 */
	public String ExecuterRequete(String chaine, String type) {
		// chargement du Driver
		
		
		try {
			// if (true)
			// throw new SQLException("bogus exception");
			//
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			// etablir une connection
			con = DriverManager.getConnection("jdbc:odbc:acdi_source", user,
					password);

			// Creation d'une instruction
			Statement requete = con.createStatement();

			// Instruction pour savoir le type de requete
			if ((type == "Inserer") || (type == "Mise") || (type == "Effacer")) {
				resultInt = requete.executeUpdate(chaine);
			}

			if (type == "Affiche") {
				result = requete.executeQuery(chaine);
			}
		} catch (ClassNotFoundException f) {
			System.out.println("ClassNotFound Exception");
			f.printStackTrace();
			return f.getMessage();
		} catch (SQLException f) {
			System.out.println("SQL Exception");
			f.printStackTrace();
			return f.getMessage();
			} catch (Exception f) {
			System.out.println("Exception");
			f.printStackTrace();
			return f.getMessage();
		}

		return RESULT_OK;
	}

	/**
	 * Receives a Beneficiary object from the GUI and uses its data to update
	 * TBS Db.
	 * 
	 * @param beneficiary
	 * @return
	 */
	public String postNewBeneficiary(Beneficiary beneficiary) {
		// TODO: Write the necessary queries and return true
		// if insert was successful
		// //////////////////Partie Informations Generales

		String Default_date = "2020/01/01";
		Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String dobString = "";
        try {
			Date dob = dateFormat.parse(beneficiary.getDob());
			dobString = dateFormat.format(dob);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String info_MCHN = beneficiary.getDossier() + "','"
				+ beneficiary.getLastName() + "','"
				+ beneficiary.getFirstName() + "','"
				+ AttributeManager.mapToLong(true, beneficiary.getCommuneSection()) + "','"
				+ beneficiary.getLocality() + "','" + Default_date + "','"
				+ dobString + "','" + beneficiary.getSex().name();

		String info_LIVELIHOOD = beneficiary.getDossier() + "','"
				+ beneficiary.getLastName() + "','"
				+ beneficiary.getFirstName() + "','"
				+ AttributeManager.mapToLong(true, beneficiary.getCommuneSection()) + "','"
				+ beneficiary.getLocality() + "','" + dateFormat.format(calendar.getTime())
				+ "','" + dobString + "','"
				+ beneficiary.getSex().name();

		System.out.println("livelihood: " + info_LIVELIHOOD);
		String requete = "";
		

		BeneficiaryType bt = beneficiary.getBeneficiaryType();
		/////////////////
		 		
		
		if (bt == BeneficiaryType.MCHN || bt == BeneficiaryType.BOTH) {
									
			try{
				   String requete121="Select * From Beneficiaire_1 where No_dossier='"+beneficiary.getDossier()+"' OR Nom='"+beneficiary.getLastName()+"' AND prenom='"+beneficiary.getFirstName()+"' AND Categorie='"+beneficiary.getBeneficiaryCategory()+"' AND Poste_distribution='"+beneficiary.getDistributionPost()+"' AND Localite_beneficiaire='"+beneficiary.getLocality()+"' OR Nom='"+beneficiary.getFirstName()+"' AND Prenom='"+beneficiary.getLastName()+"' ";
				   ExecuterRequete(requete121,"Affiche");
					 if(result.next()){
							//JOptionPane.showMessageDialog(this, "Désolez\n2 cas peuvent se présenter.\n1-Soit Ce numéro de dossier existe déja\n2- Soit <<'"+beneficiary.getLastName()+"'   '"+beneficiary.getFirstName()+"'>>  existe déja dans la base.\nDans ce cas, c'est un cas suspect. Revérifier les informations", "Existence d'informations", JOptionPane.ERROR_MESSAGE);
						 	System.out.println("This health Beneficiary already exist in tbs database ");
						 	//Rachel i want to show a message dialog box here saying that beneficiaire already existe
				            result.close();
				            return "This health beneficiary already exists in the TBS database.";
				     }
					 else{
							
							System.out.println("Insertion possible for health sector");
							
							requete = "Insert into Beneficiaire_1 values('" + info_MCHN + "','"
							+ convertBeneficiaryCategory(beneficiary.getBeneficiaryCategory()) + "','"
							+ beneficiary.getNumberInHome() + "','"
							+ beneficiary.getDistributionPost() + "','"
							+ beneficiary.getGuardianChild() + "','"
							+ beneficiary.getGuardianWoman() + "','"
							+ beneficiary.getIsMotherLeader().name() + "','"
							+ beneficiary.getVisitMotherLeader().name() + "','"
							+ beneficiary.getIsAgri().name() + "','"
							+ beneficiary.getAgriPerson() + "','" + " " + "','" + " "
							+ "','" + " " + "','" + " " + "','" + " " + "','" + " "
							+ "','" + " " + "','" + " " + "','" + " " + "','" + " "
							+ "','" + " " + "','" + " " + "','" + " " + "','" + " "
							+ "','" + " " + "','" + " " + "','" + " " + "','" + " "
							+ "','" + " " + "')";
														
							System.out.println(requete);
							String result = ExecuterRequete(requete, "Inserer");
							System.out.println(TAG + " result = " + result);
							return result;
						}
				   
			   }
			   catch (Exception f11) {
					System.out.println("Exception");
					//f11.printStackTrace();
					//return f11.getMessage();
				}
			  	
		}
		if (bt == BeneficiaryType.AGRI || bt == BeneficiaryType.BOTH) {
						
			   try{
				   String requete12="Select * From MasterList_Livelihood where No_dossier='"+beneficiary.getDossier()+"' OR Nom='"+beneficiary.getLastName()+"' AND prenom='"+beneficiary.getFirstName()+"' AND Section_communale='"+beneficiary.getCommuneSection()+"' AND Localite='"+beneficiary.getLocality()+"' OR Nom='"+beneficiary.getFirstName()+"' AND Prenom='"+beneficiary.getLastName()+"' ";
					 ExecuterRequete(requete12,"Affiche");
					 if(result.next()){
							//JOptionPane.showMessageDialog(this, "Désolez\n2 cas peuvent se présenter.\n1-Soit Ce numéro de dossier existe déja\n2- Soit <<'"+beneficiary.getLastName()+"'   '"+beneficiary.getFirstName()+"'>>  existe déja Pour:\nSection Communale et Localité/adresse mentionnés\nDans ce cas, c'est un cas suspect. Revérifier les informations", "Existence d'informations", JOptionPane.ERROR_MESSAGE);
							System.out.println("this livelihood beneficiairy already existe in tbs deja ");
							//Rachel i want to show a message dialog box here saying that beneficiaire already existe
				            result.close();
				            return "This health beneficiary already exists in the TBS database.";
				            
				     }
					 else{
							
							System.out.println("Insertion possible ");
							
							requete = "Insert into MasterList_Livelihood values('"
							+ info_LIVELIHOOD + "','"
							+ convertProfessionAttributes(beneficiary) + "','"
							+ beneficiary.getNumberInHome() + "','"
							+ beneficiary.getAmountOfLand() + "','"
							+ convertSeedAttributes(beneficiary)+ "','" 
							+ convertToolAttributes(beneficiary) + "','"
							+ convertOrganizationAttributes(beneficiary) + "','"
							+ beneficiary.getIsHealth().name() + "','"
							+ beneficiary.getHealthPerson() + "','" + " " + "')";
											
							
							System.out.println(requete);
							String result = ExecuterRequete(requete, "Inserer");
							System.out.println(TAG + " result = " + result);
							return result;
						}
				   
			   }
			   catch (Exception f1) {
					System.out.println("Exception");
					//f1.printStackTrace();
					//return f1.getMessage();
				}
			
			System.out.println("Insertion possible for livelihood");
			
		}
		System.out.println(requete);
		String result = ExecuterRequete(requete, "Inserer");
		System.out.println(TAG + " result = " + result);
		return result;

	}
	


	public String convertProfessionAttributes(Beneficiary beneficiary) {
		String professions = "";
		if (beneficiary.getIsArtisan() == AttributeManager.YnQuestion.Y)
			professions += LocaleManager.resources
					.getString(AttributeManager.FORM_ARTISAN)
					+ " "
					+ AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getIsFarmer() == AttributeManager.YnQuestion.Y)
			professions += LocaleManager.resources
					.getString(AttributeManager.FORM_FARMER)
					+ " "
					+ AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getIsMuso() == AttributeManager.YnQuestion.Y)
			professions += LocaleManager.resources
					.getString(AttributeManager.FORM_MUSO)
					+ " "
					+ AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getIsFisherman() == AttributeManager.YnQuestion.Y)
			professions += LocaleManager.resources
					.getString(AttributeManager.FORM_FISHERMAN)
					+ " "
					+ AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getIsRancher() == AttributeManager.YnQuestion.Y)
			professions += LocaleManager.resources
					.getString(AttributeManager.FORM_CATTLE_RANCHER)
					+ " "
					+ AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getIsStoreOwner() == AttributeManager.YnQuestion.Y)
			professions += LocaleManager.resources
					.getString(AttributeManager.FORM_STORE_OWNER)
					+ " "
					+ AttributeManager.ET_SEPARATOR + " ";
		if (!professions.equals(""))
			professions = professions.substring(0, professions.length() - 4); // Remove
																				// final
																				// 'et'
		return professions;
	}

	public String convertSeedAttributes(Beneficiary beneficiary) {
		String agricultureAttributes = "";
		if (beneficiary.getGetsCereal() == AttributeManager.YnQuestion.Y)
			agricultureAttributes += LocaleManager.resources
					.getString(AttributeManager.FORM_CEREAL)
					+ " "
					+ AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getGetsTrees() == AttributeManager.YnQuestion.Y)
			agricultureAttributes += LocaleManager.resources
					.getString(AttributeManager.FORM_TREE)
					+ " "
					+ AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getGetsTubers() == AttributeManager.YnQuestion.Y)
			agricultureAttributes += LocaleManager.resources
					.getString(AttributeManager.FORM_TUBERS)
					+ " "
					+ AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getGetsVeggies() == AttributeManager.YnQuestion.Y)
			agricultureAttributes += LocaleManager.resources
					.getString(AttributeManager.FORM_VEGETABLES)
					+ " "
					+ AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getGetsGrafting() == AttributeManager.YnQuestion.Y)
			agricultureAttributes += LocaleManager.resources
					.getString(AttributeManager.FORM_GRAFTING)
					+ " "
					+ AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getGetsCoffee() == AttributeManager.YnQuestion.Y)
			agricultureAttributes += LocaleManager.resources
					.getString(AttributeManager.FORM_COFFEE)
					+ " "
					+ AttributeManager.ET_SEPARATOR + " ";
		if (!agricultureAttributes.equals(""))
			agricultureAttributes = agricultureAttributes.substring(0,
					agricultureAttributes.length() - 4); // Remove final 'et'
		return agricultureAttributes;
	}

	public String convertToolAttributes(Beneficiary beneficiary){
		String agricultureAttributes = "";
		if (beneficiary.getGetsHoe() == AttributeManager.YnQuestion.Y)
			agricultureAttributes += LocaleManager.resources
					.getString(AttributeManager.FORM_HOE)
					+ " "
					+ AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getGetsMachette() == AttributeManager.YnQuestion.Y)
			agricultureAttributes += LocaleManager.resources
					.getString(AttributeManager.FORM_MACHETTE)
					+ " "
					+ AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getGetsPelle() == AttributeManager.YnQuestion.Y)
			agricultureAttributes += LocaleManager.resources
					.getString(AttributeManager.FORM_SHOVEL)
					+ " "
					+ AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getGetsPickaxe() == AttributeManager.YnQuestion.Y)
			agricultureAttributes += LocaleManager.resources
					.getString(AttributeManager.FORM_PICKAXE)
					+ " "
					+ AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getGetsSerpette() == AttributeManager.YnQuestion.Y)
			agricultureAttributes += LocaleManager.resources
					.getString(AttributeManager.FORM_PRUNING_KNIFE)
					+ " "
					+ AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getGetsWheelbarrow() == AttributeManager.YnQuestion.Y)
			agricultureAttributes += LocaleManager.resources
					.getString(AttributeManager.FORM_WHEELBARROW)
					+ " "
					+ AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getGetsBarreAMines() == AttributeManager.YnQuestion.Y)
			agricultureAttributes += LocaleManager.resources
					.getString(AttributeManager.FORM_CROWBAR)
					+ AttributeManager.ET_SEPARATOR + " ";
		if (!agricultureAttributes.equals(""))
			agricultureAttributes = agricultureAttributes.substring(0,
					agricultureAttributes.length() - 4); // Remove final 'et'
		return agricultureAttributes;
	}
	
	public String convertOrganizationAttributes(Beneficiary beneficiary) {
		String organizations = "";
		if (beneficiary.getIsFAO() == AttributeManager.YnQuestion.Y)
			organizations += LocaleManager.resources
					.getString(AttributeManager.FORM_FAO)
					+ " "
					+ AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getIsCROSE() == AttributeManager.YnQuestion.Y)
			organizations += LocaleManager.resources
					.getString(AttributeManager.FORM_CROSE)
					+ " "
					+ AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getIsMARDNR() == AttributeManager.YnQuestion.Y)
			organizations += LocaleManager.resources
					.getString(AttributeManager.FORM_MARDNR)
					+ " "
					+ AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getIsPLAN() == AttributeManager.YnQuestion.Y)
			organizations += LocaleManager.resources
					.getString(AttributeManager.FORM_PLAN)
					+ " "
					+ AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getIsSAVE() == AttributeManager.YnQuestion.Y)
			organizations += LocaleManager.resources
					.getString(AttributeManager.FORM_SAVE_ORG)
					+ " "
					+ AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getIsOrganizationOther() == AttributeManager.YnQuestion.Y)
			organizations += LocaleManager.resources
					.getString(AttributeManager.FORM_OTHER_ORG)
					+ " "
					+ AttributeManager.ET_SEPARATOR + " ";
		if (!organizations.equals(""))
			organizations = organizations.substring(0,
					organizations.length() - 4); // Remove final 'et'
		return organizations;
	}

	private String convertBeneficiaryCategory(BeneficiaryCategory category){
		String convertedCategory = "";
		if (category == AttributeManager.BeneficiaryCategory.EXPECTING)
			convertedCategory = "Femme Enceinte";
		else if (category == AttributeManager.BeneficiaryCategory.NURSING)
			convertedCategory = "Femme Allaitante";
		else if (category == AttributeManager.BeneficiaryCategory.PREVENTION)
			convertedCategory = "Enfant Prevention";
		else if (category == AttributeManager.BeneficiaryCategory.MALNOURISHED)
			convertedCategory = "Enfant Mal Nouri";
		return convertedCategory;
	}
	public static void main(String args[]) {
		TbsManager tbs = new TbsManager();
		tbs.readConfigFile();
	}
}
