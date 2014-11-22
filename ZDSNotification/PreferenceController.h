//
//  PreferenceController.h
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 28/08/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//

#import <Cocoa/Cocoa.h>
#import "ParserZSD.h"
@class ConnectonParse;

@interface PreferenceController : NSWindowController{
    IBOutlet NSPanel *hudWindows;
    int time;
    
}
@property (weak) IBOutlet NSImageView *imageView;
@property (assign) bool imageAffiche;
@property (weak) IBOutlet NSTextField *labelPseudos;

@property (weak) IBOutlet NSTextField *labelTime;
@property (retain) ParserZSD *parser;

- (IBAction)quitPanel:(id)sender;
+ (BOOL) preferenceImageNotification;
+ (void)setPreferenceImageNotification: (BOOL)a;
+(float)preferenceRefresh;
+ (void) setPreferenceRefresh: (float)f;
+ (NSString *)preferenceVersion;
+ (void)setPreferenceVersion:(NSString *)v;

- (void) setTime:(int)t;
- (int)time;


@end
