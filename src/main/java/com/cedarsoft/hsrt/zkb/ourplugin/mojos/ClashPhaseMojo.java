package com.cedarsoft.hsrt.zkb.ourplugin.mojos;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 25.10.13
 * Time: 20:26
 * To change this template use File | Settings | File Templates.
 */


import com.cedarsoft.hsrt.zkb.ourplugin.DependencyService;
import com.cedarsoft.hsrt.zkb.ourplugin.model.ClashCollectResultWrapper;
import com.cedarsoft.hsrt.zkb.ourplugin.visualize.ConsoleVisualizer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;

/**
 * Get all Dependencies of the project
 */
//clashinspector
//clashfinder
@Mojo(name = "listPhase", requiresProject = true, defaultPhase = LifecyclePhase.COMPILE)
public class ClashPhaseMojo extends AbstractClashMojo {

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    super.execute();

    try {
      Artifact artifact = new DefaultArtifact( this.getProject().getArtifact().toString() );

      DependencyService dependencyService = new DependencyService();
      ConsoleVisualizer consoleVisualizer = new ConsoleVisualizer();

      ClashCollectResultWrapper clashCollectResultWrapper = new ClashCollectResultWrapper( dependencyService.getDependencyTree( artifact, this.getRepoSession(), this.getRepoSystem(), this.getIncludedScopesList(), this.getExcludedScopesList(), this.isIncludeOptional() ) );

      if ( clashCollectResultWrapper.hasVersionClash( this.getClashDetectionLevel() ) ) {
        consoleVisualizer.visualize( clashCollectResultWrapper, this.getClashDetectionLevel(), this );
        throw new MojoExecutionException( "Version Clashes for Detection-Level " + this.getClashDetectionLevel() + " detected!!" );
      }

    } catch ( IllegalArgumentException e ) {
      throw new MojoFailureException( e.getMessage(), e );
    }
  }
}


