//
//  AppController.m
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 28/08/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//

#import "AppController.h"
#import  "PreferenceController.h"
NSString  * const CSlider = @"CSlider";
NSString  * const CNotificationPreferenceImage = @"CNotificationPreferenceImage";


@implementation AppController


-(IBAction)showPreferencePanel:(id)sender{
    
    if (!preferenceController) {
		preferenceController = [[PreferenceController alloc] init];
	}
	NSLog(@"showing %@", preferenceController);
	[preferenceController showWindow:self];
}

+(void)initialize{
    NSMutableDictionary *defaultValue = [NSMutableDictionary dictionary];
    BOOL i = YES;
    [defaultValue setObject:[NSNumber numberWithFloat:5.0] forKey:CSlider];
    [defaultValue setObject: [NSNumber numberWithBool:i] forKey:CNotificationPreferenceImage];
    
    NSLog(@"avant : %@",[defaultValue objectForKey:CNotificationPreferenceImage]);
        
    [[NSUserDefaults standardUserDefaults] registerDefaults:defaultValue];
    NSLog(@"après : %@",[[NSUserDefaults standardUserDefaults] objectForKey:CNotificationPreferenceImage]);
    NSLog(@"Normalement les valeurs insrites :%@",defaultValue);
}

@end
