//
//  PreferenceController.h
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 28/08/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//

#import <Cocoa/Cocoa.h>
#import "ITSwitch/ITSwitch.h"
@class ConnectonParse;

@interface PreferenceController : NSWindowController{
    IBOutlet NSSlider *timeSlider;
    IBOutlet NSImageView *imageView;
    IBOutlet NSTextField *labelConnection;
    IBOutlet NSTextField *labelRafraichissement;
    IBOutlet ITSwitch *checkImage;
    __weak IBOutlet NSButton *buttonRelaunch;

    __weak IBOutlet NSProgressIndicator *progressIndicator;
    


    

    
    ConnectonParse *connexion;
    NSImage *imagePseudos;
  

}

- (IBAction) changeTimeRefresh:(id)sender;
- (IBAction)changeSwitchImage:(ITSwitch *)sender;

+ (BOOL) preferenceImageNotification;
+ (void)setPreferenceImageNotification: (BOOL)a;
+(float)preferenceRefresh;
+ (void) setPreferenceRefresh: (float)f;
+ (NSString *)preferenceVersion;
+ (void)setPreferenceVersion:(NSString *)v;
- (IBAction)Relance:(id)sender;
-(void)chargeImage:(id)param;
-(void)update;



@end
