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
    __weak IBOutlet NSSlider *timeSlider;
    __weak IBOutlet NSImageView *imageView;
    __weak IBOutlet NSTextField *labelConnection;
    __weak IBOutlet NSTextField *labelRafraichissement;
    
    __weak IBOutlet NSButton *buttonRelaunch;

    __weak IBOutlet NSProgressIndicator *progressIndicator;
    
    IBOutlet NSPanel *hudWindows;
    __weak IBOutlet NSButton *checkImage;





    

    
    ConnectonParse *connexion;
    NSImage *imagePseudos;
  

}
- (void)updateTime:(float)time;
- (IBAction) changeTimeRefresh:(id)sender;
- (IBAction)changeSwitchImage:(id)sender;

- (IBAction)quitPanel:(id)sender;

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
