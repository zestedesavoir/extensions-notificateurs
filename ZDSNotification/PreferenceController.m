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
    
    return self;
}

- (void)windowDidLoad{
    NSLog(@"Nib file is loaded");
    
    [super windowDidLoad];
    
    [timeSlider setFloatValue:[PreferenceController preferenceRefresh]];
    [checkImage setOn:[PreferenceController preferenceImageNotification]];
    NSString *refresh = [[NSString alloc] initWithFormat:@"Rafraichissement toutes %f secondes.",[PreferenceController preferenceRefresh]];
    [labelRafraichissement setStringValue:refresh];
    
    NSLog(@"%hhd",[PreferenceController preferenceImageNotification]);
       
    
}


- (IBAction)changeTimeRefresh:(id)sender{
    NSString *refresh = [[NSString alloc] initWithFormat:@"Rafraichissement toutes %@ secondes.",[sender stringValue]];
    [labelRafraichissement setStringValue:refresh];
    [PreferenceController setPreferenceRefresh:[sender floatValue]];
}



- (IBAction)changeSwitchImage:(ITSwitch *)sender{
    NSLog(@"Value check  %d",[sender isOn]);
    [PreferenceController setPreferenceImageNotification:[sender isOn]];
}


- (void) awakeFromNib{
    
   
    ConnectonParse *connexion = [[ConnectonParse alloc] init];
    if ([connexion isConnextionZDS]){
                NSImage *imagePseudos = [[NSImage alloc] initByReferencingURL:[NSURL URLWithString:[connexion getUrlImagePseudos]]];
        
        NSString *pseudo = [[NSString alloc]initWithFormat:@"%@, vous êtes connectés.",[connexion getPseudosZDS]];
       
        [imageView setImage:imagePseudos];
        [labelConnection setStringValue:pseudo];
        
    }else{
        [imageView setImage:[NSImage imageNamed:@"clem_pas_contente.png"]];
        [labelConnection setStringValue:@"Vous n'êtes pas connectés."];
    }

    
}
+ (BOOL)preferenceImageNotification{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSLog(@"%@",[defaults objectForKey:CNotificationPreferenceImage]);
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

 
@end
