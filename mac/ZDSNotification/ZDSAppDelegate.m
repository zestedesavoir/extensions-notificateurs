//
//  ZDSAppDelegate.m
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 26/08/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//

#import "ZDSAppDelegate.h"
@class ParserZSD;


@implementation ZDSAppDelegate

- (void)applicationDidFinishLaunching:(NSNotification *)aNotification
{
    [[NSUserNotificationCenter defaultUserNotificationCenter] setDelegate:self];
}

- (id) init{
    self = [super init];
    if (self){
        _allNotification = [[NSMutableArray alloc]init];
       NSTimer *time =  [NSTimer scheduledTimerWithTimeInterval:10.0
                                         target:self
                                       selector:@selector(checkNew)
                                       userInfo:nil
                                        repeats:YES];
        
        [self checkNew];
    }
    return self;
}
- (void) awakeFromNib{
    statusItem = [[NSStatusBar systemStatusBar] statusItemWithLength:NSVariableStatusItemLength];
    [statusItem setMenu:statusMenu];
    [statusItem setHighlightMode:YES];
    [statusItem setImage:[NSImage imageNamed:@"icone_19.png"]];
}

- (NSUserNotification *) showNotficationWithTitle:(NSString *)title AndWithInformation :(NSString *)infomation withImageAuteurs: (NSString *)url{
    NSImage *image = [[NSImage alloc] initWithContentsOfURL:[NSURL URLWithString:url]];
    NSUserNotification *notification = [[NSUserNotification alloc] init];
    notification.title = title;
    notification.informativeText = infomation;
    notification.soundName = NSUserNotificationDefaultSoundName;
    [notification setContentImage:image];
    [[NSUserNotificationCenter defaultUserNotificationCenter] deliverNotification:notification];
    
    return notification;
}
- (void)userNotificationCenter:(NSUserNotificationCenter *)center didActivateNotification:(NSUserNotification *)notification{
    for (NSMutableArray *array in _allNotification){
        if ([notification isEqualTo:[array objectAtIndex:4]]){
            NSLog(@"Le lien est trouvé");
            NSURL *url = [NSURL URLWithString:[array objectAtIndex:2]];
            [[NSWorkspace sharedWorkspace]openURL:url];
        }
    }
      
}

- (BOOL)userNotificationCenter:(NSUserNotificationCenter *)center shouldPresentNotification:(NSUserNotification *)notification{
    return YES;
}

- (void) checkNew{
    ParserZSD *parse = [[ParserZSD alloc] init];
    
    NSMutableArray *infosNotifier = [parse parseZDS];
    
    
        for(NSMutableArray *array in infosNotifier){
            if (![self findArrayInArray:_allNotification withArray:array]){

                 NSUserNotification * notification = [self showNotficationWithTitle:[array objectAtIndex:0] AndWithInformation:[NSString stringWithFormat:@"%@ a répondu à votre message",[array objectAtIndex:1]] withImageAuteurs:[array objectAtIndex:3]];
                NSLog(@"%@",array);
                [array addObject:notification];
        
                [_allNotification addObject:array];
        }
    }
    
    

     
}

-(BOOL)findArrayInArray:(NSMutableArray *)arrayInArray withArray:(NSMutableArray *)arrayValue{
    BOOL findIsYes = NO;
    
    for (NSMutableArray *arrayIn in arrayInArray){
        
        if ([[arrayIn objectAtIndex:2] isEqualToString:[arrayValue objectAtIndex:2]]){ //C'est le lien qui fait toute la difference
            findIsYes = YES;
            
            
        }
         
    }
    return findIsYes;
}


@end
