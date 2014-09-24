//
//  PreferenceController.h
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 28/08/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//

#import <Cocoa/Cocoa.h>
#import "ITSwitch/ITSwitch.h"

@interface PreferenceController : NSWindowController{
    IBOutlet NSSlider *timeSlider;
    IBOutlet NSImageView *imageView;
    IBOutlet NSTextField *labelConnection;
    IBOutlet NSTextField *labelRafraichissement;
    IBOutlet ITSwitch *checkImage;

  

}

- (IBAction) changeTimeRefresh:(id)sender;
- (IBAction)changeSwitchImage:(ITSwitch *)sender;

+ (BOOL) preferenceImageNotification;
+ (void)setPreferenceImageNotification: (BOOL)a;
+(float)preferenceRefresh;
+ (void) setPreferenceRefresh: (float)f;
- (IBAction)Relance:(id)sender;



@end
