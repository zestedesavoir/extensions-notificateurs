//
//  PreferenceController.m
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 28/08/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//

#import "PreferenceController.h"
#import "AppController.h"


@implementation PreferenceController
@synthesize imageAffiche;
- (id) init{
    self = [super initWithWindowNibName:@"Preference"];
    _parser = [[ParserZSD alloc] initForInformationUtilisateur]; //Instance du parse;
    return self;
}

- (void)windowDidLoad{
    [super windowDidLoad];
}


- (IBAction)quitPanel:(id)sender {
    [hudWindows close];
    [PreferenceController setPreferenceRefresh:[self time]];
    [PreferenceController setPreferenceImageNotification:[self imageAffiche]];
         //On envoie une notification au Parser comme quoi il faut modifier la valeur
    NSNotificationCenter *nn = [NSNotificationCenter defaultCenter];
    [nn postNotificationName:@"updateTime" object:nil];
    

}



- (void) awakeFromNib{
  
    [self performSelectorInBackground:@selector(chargeImage:) withObject:nil];
    
    [self update];
    
    NSButton *closeButton = [hudWindows standardWindowButton:NSWindowCloseButton];
    [closeButton setKeyEquivalentModifierMask:NSCommandKeyMask];
    [closeButton setKeyEquivalent:@"w"];
    [closeButton setTarget:self];
    [closeButton setAction:@selector(quitPanel:)];
     [self setTime:[PreferenceController preferenceRefresh]];
    [self setImageAffiche:[PreferenceController preferenceImageNotification]];
    
    
    
    
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

- (void) setTime:(int)t{
    time = t;
    [self updateTime:(float) time];
}

- (int)time{
    return time;
}
-(void)chargeImage:(id)param{
   
    @autoreleasepool {
        NSArray * result=nil;
        NSImage *image = [[NSImage alloc] initByReferencingURL:[_parser getUrlImagePseudos ]];
        
        if (image != nil)
        [_imageView setImage:image];
        else
        [_imageView setImage:[NSImage imageNamed:@"clem_pas_contente.png"]];

        [self performSelectorOnMainThread:@selector(update) withObject:result waitUntilDone:NO ];
        
        
    }
    

    
}



-(void) updateTime:(float)time{
    
    int minute = ceil(time /60);
    
    if (minute > 1 ){
        NSString *refresh = [[NSString alloc] initWithFormat:@"Rafraichissement toutes les %d minutes.",minute];
        [_labelTime setStringValue:refresh];
        
        
        
    }else{
        NSString *refresh;
        long secondes = lround((long)time);
        if (secondes > 1){
            refresh = [[NSString alloc] initWithFormat:@"Rafraichissement toutes les %ld secondes.",secondes];
            
        }else{
            
            refresh = [[NSString alloc] initWithFormat:@"Rafraichissement toutes les %ld seconde.",secondes];
            
            
        }
        [_labelTime setStringValue:refresh];
    }
    
    
  
    
    
}

-(void)update{
    if ([_parser isConnextionZDS] || [_parser isConnextionZDS] !=0){
        
            NSString *pseudo = [[NSString alloc]initWithFormat:@"%@, vous êtes connecté.",[_parser  getPseudosZDS]];
        
        [_labelPseudos setStringValue:pseudo];
        
    }else{
                [_labelPseudos setStringValue:@"Vous n'êtes pas connectés."];
    }

    
}
 
@end
