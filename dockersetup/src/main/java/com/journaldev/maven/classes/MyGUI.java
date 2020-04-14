package com.journaldev.maven.classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.FileInputStream;

import com.google.api.gax.paging.Page;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.dataproc.Dataproc;
import com.google.api.services.dataproc.DataprocScopes;
import com.google.api.services.dataproc.model.HadoopJob;
import com.google.api.services.dataproc.model.Job;
import com.google.api.services.dataproc.model.JobPlacement;
import com.google.api.services.dataproc.model.SubmitJobRequest;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.model.Bucket;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage.BlobListOption;
import com.google.cloud.storage.StorageOptions;
import com.google.common.collect.ImmutableList;

public class MyGUI {
	
	public static JFrame loadWindow = new JFrame("Inverted Index Startup");;
	
	public static void main(String[] args) {
		
		ActionListener listen = new Listener();

		System.out.println("Made it");
		File tmpDir = new File("/Users/ben/Desktop/bcg36-credentials.json");
		boolean exists = tmpDir.exists();
		System.out.println("exists");
		
		/*
		 * This creates the original panal on load that will allow the user to select what 
		 * files they want to cluster to collect data on.
		 */
		//loadWindow = new JFrame("Inverted Index Startup");
		loadWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loadWindow.setSize(600,600);

		/*
		 * This is the panel in which we set the layout of the GUI to have 5 rows and 1 column
		 */
		JPanel content = new JPanel();
		content.setBackground(Color.PINK);
		content.setLayout(new GridLayout(5,1));
		loadWindow.getContentPane().add(content);
		
		/*
		 * This section create the labels and the then the 4 buttons that show the user the options
		 * that the program can run.
		 */
		JLabel text = new JLabel("<html><span style='font-size: 25px;'>MapReduce: Inverted Index</span></html>", SwingConstants.CENTER);
		JButton hugo = new JButton("Run Program on Hugo");
		hugo.addActionListener(listen);
		JButton shakespeare = new JButton("Run Program on Shakespeare");
		shakespeare.addActionListener(listen);
		JButton tolstoy = new JButton("Run Program on Tolstoy");
		tolstoy.addActionListener(listen);
		JButton all = new JButton("Run Program on All Files");
		all.addActionListener(listen);

		/*
		 * This section adds all of the buttons and text to the main panel of the GUI
		 */
		content.add(text);
		content.add(hugo);
		content.add(shakespeare);
		content.add(tolstoy);
		content.add(all);

		loadWindow.setVisible(true);


	}
	
	private static class Listener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            if (e.getActionCommand().equals("Run Program on Hugo")) {

                try {
                    submitHugo();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }


            }
            
            if (e.getActionCommand().equals("Run Program on Shakespeare")) {

                try {
                    submitShakespeare();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }


            }
            
            if (e.getActionCommand().equals("Run Program on Tolstoy")) {

                try {
                    submitTolstoy();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
            
            if (e.getActionCommand().equals("Run Program on All Files")) {

                try {
                    submitAll();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
            System.out.print("Got Here");
            String data ="";
            while (true) {
                try {
                    data = getOutputData("output/output.txt");
                    System.out.println(data);
                    break;
                } catch (Exception e1) {
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                }
            }
            addToGui(data);
        }
    }
	
	public static void addToGui(String data) {
		loadWindow.getContentPane().removeAll();
		JTextArea fileNamesList = new JTextArea(500, 500);
        fileNamesList.append(data);
        fileNamesList.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane (fileNamesList);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(500, 500));
        scrollPane.setMinimumSize(new Dimension(500, 500));
        
        loadWindow.add(scrollPane);
	}
	
	public static void submitHugo() throws IOException {
        Dataproc dataproc = new Dataproc.Builder(new NetHttpTransport(), new JacksonFactory(), 
            new HttpCredentialsAdapter(GoogleCredentials.getApplicationDefault().createScoped(DataprocScopes.all())))
            .setApplicationName("InvertedInex").build();
        dataproc.projects().regions().jobs().submit("bcg36-1660-courseproject", "us-west1", new SubmitJobRequest()
            .setJob(new Job().setPlacement(new JobPlacement().setClusterName("graybill-1660-cluster"))
            .setHadoopJob(new HadoopJob().setMainClass("InvertedIndex")
                .setJarFileUris(ImmutableList.of("gs://dataproc-staging-us-west1-1057492895856-va2i33qh/JAR/invertedindex.jar"))
                .setArgs(ImmutableList.of(
                    "gs://dataproc-staging-us-west1-1057492895856-va2i33qh/dev_data/Hugo", "gs://dataproc-staging-us-west1-1057492895856-va2i33qh/output")))))
        .execute();
    }
	
	public static void submitShakespeare() throws IOException {
        Dataproc dataproc = new Dataproc.Builder(new NetHttpTransport(), new JacksonFactory(), 
            new HttpCredentialsAdapter(GoogleCredentials.getApplicationDefault().createScoped(DataprocScopes.all())))
            .setApplicationName("InvertedInex").build();
        dataproc.projects().regions().jobs().submit("bcg36-1660-courseproject", "us-west1", new SubmitJobRequest()
            .setJob(new Job().setPlacement(new JobPlacement().setClusterName("graybill-1660-cluster"))
            .setHadoopJob(new HadoopJob().setMainClass("InvertedIndex")
                .setJarFileUris(ImmutableList.of("gs://dataproc-staging-us-west1-1057492895856-va2i33qh/JAR/invertedindex.jar"))
                .setArgs(ImmutableList.of(
                    "gs://dataproc-staging-us-west1-1057492895856-va2i33qh/dev_data/shakespeare", "gs://dataproc-staging-us-west1-1057492895856-va2i33qh/output")))))
        .execute();
    }
	
	public static void submitTolstoy() throws IOException {
        Dataproc dataproc = new Dataproc.Builder(new NetHttpTransport(), new JacksonFactory(), 
            new HttpCredentialsAdapter(GoogleCredentials.getApplicationDefault().createScoped(DataprocScopes.all())))
            .setApplicationName("InvertedInex").build();
        dataproc.projects().regions().jobs().submit("bcg36-1660-courseproject", "us-west1", new SubmitJobRequest()
            .setJob(new Job().setPlacement(new JobPlacement().setClusterName("graybill-1660-cluster"))
            .setHadoopJob(new HadoopJob().setMainClass("InvertedIndex")
                .setJarFileUris(ImmutableList.of("gs://dataproc-staging-us-west1-1057492895856-va2i33qh/JAR/invertedindex.jar"))
                .setArgs(ImmutableList.of(
                    "gs://dataproc-staging-us-west1-1057492895856-va2i33qh/dev_data/Tolstoy", "gs://dataproc-staging-us-west1-1057492895856-va2i33qh/output")))))
        .execute();
    }
	
	public static void submitAll() throws IOException {
        Dataproc dataproc = new Dataproc.Builder(new NetHttpTransport(), new JacksonFactory(), 
            new HttpCredentialsAdapter(GoogleCredentials.getApplicationDefault().createScoped(DataprocScopes.all())))
            .setApplicationName("InvertedInex").build();
        dataproc.projects().regions().jobs().submit("bcg36-1660-courseproject", "us-west1", new SubmitJobRequest()
            .setJob(new Job().setPlacement(new JobPlacement().setClusterName("graybill-1660-cluster"))
            .setHadoopJob(new HadoopJob().setMainClass("InvertedIndex")
                .setJarFileUris(ImmutableList.of("gs://dataproc-staging-us-west1-1057492895856-va2i33qh/JAR/invertedindex.jar"))
                .setArgs(ImmutableList.of(
                    "gs://dataproc-staging-us-west1-1057492895856-va2i33qh/all_data", "gs://dataproc-staging-us-west1-1057492895856-va2i33qh/output")))))
        .execute();
    }
	
	public static String getOutputData(String outputFile) throws Exception{
		System.out.println("Try getting file");
        String projectId = "bcg36-1660-courseproject";
        String bucketName = "dataproc-staging-us-west1-1057492895856-va2i33qh";
        Storage storage = (Storage) StorageOptions.newBuilder().setProjectId(projectId).setCredentials(GoogleCredentials.getApplicationDefault()).build().getService();
        Blob blob = ((com.google.cloud.storage.Storage) storage).get(bucketName, outputFile);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        blob.downloadTo(byteStream);
        int arrayLength = byteStream.size();
        byte[] finalMerge = byteStream.toByteArray();
        String outputData = new String(finalMerge);
        return outputData;
        
    }
    

    
}