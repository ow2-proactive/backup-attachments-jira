import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.provider.sftp.SftpFileSystemConfigBuilder;


public class TestSFTP {

	public static void main(String[] args) {
		FileSystemManager fsManager = null;
		
		
		 try {
			 FileSystemOptions fsOptions = new FileSystemOptions();
			 SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(fsOptions, "no");
			 SftpFileSystemConfigBuilder.getInstance().setTimeout(fsOptions, 10000);
			 SftpFileSystemConfigBuilder.getInstance().setKnownHosts(fsOptions, new File("C:\\cygwin64\\home\\GR208812\\.ssh\\known_hosts"));
			 //SftpFileSystemConfigBuilder.getInstance().setPassiveMode(fsOptions, false);
		     fsManager = VFS.getManager();
		 } catch (FileSystemException e) {
		     // TODO Auto-generated catch block
			 e.printStackTrace();
			 System.err.println("Could not create Default FileSystem Manager");
		 }
		 
		 String patchdir="sftp://GR208812@132.167.129.10/tmp/patch"; 
		 String patchdir2="sftp://132.167.129.10/tmp/patch"; 
		 
		String RemoteServer = "sftp://GR208812@132.167.129.10/";
        FileObject vfserveurRemote=null, vfserveurLocal = null;
		try {
			vfserveurRemote = fsManager.resolveFile(patchdir+"/PASversion.txt");
			//vfserveurRemote = fsManager.resolveFile(RemoteServer+"ChangeScheduler.jar");
			vfserveurLocal = fsManager.resolveFile(new File("D://CIVA_RP/d_DC11/c-r_DC11/javaWorkArea/ceaProActiveExtension/ressources/proactive/production/PASversionRemote.txt").toURI().toURL().toString());
			vfserveurLocal.copyFrom(vfserveurRemote, org.apache.commons.vfs.Selectors.SELECT_SELF);
		} catch (FileSystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        BufferedReader reader = null;  
        String vserveur = null;
        try {
            reader = new BufferedReader(new InputStreamReader(vfserveurRemote.getContent().getInputStream()));
            vserveur = reader.readLine();           
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }  
        }

	}

}
