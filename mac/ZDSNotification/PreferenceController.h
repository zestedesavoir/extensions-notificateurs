//
//  PreferenceController.h
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 28/08/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//

#import <Cocoa/Cocoa.h>

@interface PreferenceController : NSWindowController{
    IBOutlet NSSlider *timeSlider;
    IBOutlet NSImageView *imageView;
    IBOutlet NSTextField *labelConnection;
}

- (IBAction) changeTimeRefresh:(id)sender;


@end
