package edu.iastate.cs319.javapainter;


//TODO: Add -h/--help option and print options out to stdout
//TODO: Try and change the cursor of the line to a pencil or dot.
//TODO: Try and change the disabled cursor from move to disabled
//TODO: Fix implementation of DrawingCanvas to allow drawing when dragging up.
//TODO: Fix when the look & feel changes back to default

public class Boot {

    public static void main(String[] args) {
        
        JavaPainter painter = new JavaPainter();
        
        if(args.length > 0) {
            for(int i = 0; i < args.length; i++) {
                String arg = args[i].trim().toLowerCase();
                if(arg.compareTo("-extra") == 0 || arg.compareTo("--extra-features=true") == 0) {
                    painter.setExtraFeaturesEnabled(true);
                }
                else if(arg.compareTo("-napkin") == 0 || arg.compareTo("--look-and-feel=napkin")==0) {
                    painter.setNapkinLookAndFeel(true);
                }
            }
        }
    
        painter.setVisible(true);
        
    }
  
}
