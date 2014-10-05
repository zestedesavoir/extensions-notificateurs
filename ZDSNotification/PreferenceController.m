//
//  PreferenceController.m
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 28/08/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//

#import "PreferenceController.h"
#import "ConnectonParse.h"
#import "AppController.h"


@implementation PreferenceController

- (id) init{
    self = [super initWithWindowNibName:@"Preferences"];
    connexion = [[ConnectonParse alloc] init];
    
    return self;
}

- (void)windowDidLoad{
    
    [super windowDidLoad];
    
    [timeSlider setFloatValue:[PreferenceController preferenceRefresh]];
    [checkImage setOn:[PreferenceController preferenceImageNotification]];
    NSString *refresh = [[NSString alloc] initWithFormat:@"Rafraichissement toutes %f secondes.",[PreferenceController preferenceRefresh]];
    [labelRafraichissement setStringValue:refresh];
    
    [buttonRelaunch setEnabled:NO];

       
    
}


- (IBAction)changeTimeRefresh:(id)sender{
    NSString *refresh = [[NSString alloc] initWithFormat:@"Rafraichissement toutes %@ secondes.",[sender stringValue]];
    [labelRafraichissement setStringValue:refresh];
    [PreferenceController setPreferenceRefresh:[sender floatValue]];
    [buttonRelaunch setEnabled:YES];
    
}



- (IBAction)changeSwitchImage:(ITSwitch *)sender{
    [PreferenceController setPreferenceImageNotification:[sender isOn]];
}


- (void) awakeFromNib{
    [self performSelectorInBackground:@selector(chargeImage:) withObject:nil];
    
    
    [self update];
    
    
}
+ (BOOL)preferenceImageNotification{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
       return [defaults boolForKey:CNotificationPreferenceImage];
}

+ (void) setPreferenceImageNotification: (BOOL)a{
    [[NSUserDefaults standardUserDefaults] setBool:a forKey:CNotificationPreferenceImage];
}

+ (float)preferenceRefresh{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    return [defaults floatForKey:CSlider];
}
+ (void) setPreferenceRefresh:(float)f{
    [[NSUserDefaults standardUserDefaults] setFloat:f forKey:CSlider];
}
+ (void)setPreferenceVersion:(NSString *)v{
    [[NSUserDefaults standardUserDefaults] setObject:v forKey:CVersion];
}
+ (NSString *)preferenceVersion{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    return [defaults objectForKey:CVersion];
}

- (IBAction)Relance:(id)sender {
    
    int processIdentifier = [[NSProcessInfo processInfo] processIdentifier];
    NSString *myPath = [NSString stringWithFormat:@"%s",
                        [[[NSBundle mainBundle] executablePath] fileSystemRepresentation]];
    [NSTask launchedTaskWithLaunchPath:myPath arguments:[NSArray
                                                         arrayWithObject:[NSString stringWithFormat:@"%d",
                                                                          processIdentifier]]];
    [NSApp terminate:self];
}
-(void)chargeImage:(id)param{
   
    @autoreleasepool {
        NSArray * result=nil;
        imagePseudos = [[NSImage alloc] initByReferencingURL:[NSURL URLWithString:[connexion getUrlImagePseudos]]];
        
        [self performSelectorOnMainThread:@selector(update) withObject:result waitUntilDone:NO ];
        
        
    }
    

    
}
-(void)update{
    if ([connexion isConnextionZDS] || [connexion isConnextionZDS] !=nil){
        
        if (imagePseudos ==nil){
            [progressIndicator setUsesThreadedAnimation:YES];
            [progressIndicator startAnimation:nil];
        }else{
            [progressIndicator stopAnimation:nil];
            [progressIndicator setHidden:YES];
           
            
            [imageView setImage:imagePseudos];
            
        }
        NSString *pseudo = [[NSString alloc]initWithFormat:@"%@, vous êtes connecté.",[connexion getPseudosZDS]];
        
        [labelConnection setStringValue:pseudo];
        
    }else{
        [imageView setImage:[NSImage imageNamed:@"clem_pas_contente.png"]];
        [labelConnection setStringValue:@"Vous n'êtes pas connectés."];
    }

    
}
 
@end
