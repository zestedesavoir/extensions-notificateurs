//
//  ParserZSD.m
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 26/08/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//

#import "ParserZSD.h"
#import "PreferenceController.h"

@class HTMLNode;
@class HTMLParser;
NSString * const ZDSUpdateNotification = @"ZDSUpdateNotification";
@implementation ParserZSD
@synthesize timer;
@synthesize parser;


- (id) init{
    self = [super init];
    
    if (self){
        _infosParserTopic = [[NSMutableArray alloc]init];
        article = [[NSMutableArray alloc] init];
        
        NSURL *tutorialsUrl = [NSURL URLWithString:@"http://zestedesavoir.com/"];
        
        NSData *tutorialsHtmlData = [NSData dataWithContentsOfURL:tutorialsUrl];
        if (!tutorialsHtmlData){
            NSAlert *alertInternet = [NSAlert alertWithMessageText:@"Erreur de connexion" defaultButton:@"Quitter" alternateButton:nil otherButton:nil informativeTextWithFormat:@"Vous n'êtes pas connécté à Internet"];
            [alertInternet runModal];
            exit(173);
        }
        NSString *string = [[NSString alloc] initWithUTF8String:[tutorialsHtmlData bytes]];
        while (!parser){
        NSError *error = nil;
            parser = [[HTMLParser alloc] initWithString:string error:&error];
        }
        [self startTimer]; //Lancement du timer
        [self parseZDS]; //Parsing du zds
        
        
           }
    return self;
}

- (id) initForInformationUtilisateur{
    self = [super init];
    if (self){
        _infosParserTopic = [[NSMutableArray alloc]init];
        article = [[NSMutableArray alloc] init];
        
        NSURL *tutorialsUrl = [NSURL URLWithString:@"http://zestedesavoir.com/"];
        
        NSData *tutorialsHtmlData = [NSData dataWithContentsOfURL:tutorialsUrl];
        NSString *string = [[NSString alloc] initWithUTF8String:[tutorialsHtmlData bytes]];
        while (!parser){
            NSError *error = nil;
            parser = [[HTMLParser alloc] initWithString:string error:&error];
        }
        //Observateur
        NSNotificationCenter *nn = [NSNotificationCenter defaultCenter];
        [nn addObserver:self selector:@selector(reStartTimer) name:@"updateTime" object:nil];


    }
    return self;
}
- (void)parseZDS{
    // Example : file:///Users/Odric/Documents/ZDSNotificationHtml.html
    // Exampe : file:///Users/Odric/Documents/ZDSAvec1Message.html
    // Lien du site :http://zestedesavoir.com
    
    NSURL *tutorialsUrl = [NSURL URLWithString:@"http://zestedesavoir.com/"];
    
    NSData *tutorialsHtmlData = [NSData dataWithContentsOfURL:tutorialsUrl];
    NSString *string = [[NSString alloc] initWithUTF8String:[tutorialsHtmlData bytes]];
   
        NSError *error = nil;
        HTMLParser *parser1 = [[HTMLParser alloc] initWithString:string error:&error];
  


    
    NSLog(@"Parsing");
    
    
    //Partie Topic
    HTMLNode *bodyNode = [parser1 body];
    NSArray *inputNodes = [bodyNode findChildTags:@"li"]; //On cherche tout les blocs <li>
   
    
    
    
    
    for (HTMLNode *input in inputNodes){
         NSMutableArray *array = [[NSMutableArray alloc] init];
            if ([[input rawContents] rangeOfString:@"username"].location != NSNotFound){ //On cherche dans les blocs le mot username
            //On fait un liste qui contient tout les informations
            
            
            HTMLParser *parserSecondaire = [[HTMLParser alloc] initWithString:[input rawContents] error:nil];// On parse le bloc
            NSArray *span = [[parserSecondaire body] findChildTags:@"span"];
            
            //On trouve le titre
            if ([[[span objectAtIndex:2] getAttributeNamed:@"class"] isEqualToString:@"topic"]){
                
                [array addObject:[self parseString:[[span objectAtIndex:2] rawContents]]];
                 NSLog(@"%@",[[span objectAtIndex:2] rawContents]);
               
                
            } //L'auteur
            if ([[[span objectAtIndex:0] getAttributeNamed:@"class"] isEqualToString:@"username"]){
                [array addObject:[self parseString:[[span objectAtIndex:0] rawContents]]];
               

                
                
            }
            
            //On trouve l'url
            NSArray *inputSecondaireHref = [[parserSecondaire body] findChildTags:@"a"];
           
            [array addObject:[@"http://zestedesavoir.com" stringByAppendingString:[[inputSecondaireHref objectAtIndex:0] getAttributeNamed:@"href"]]];
            
            //On trouve l'url de l'image de l'auteur.
            NSArray *image = [[parserSecondaire body] findChildTags:@"img"];
            NSString *lienImageAuteur = [[image objectAtIndex:0] getAttributeNamed:@"src"];
            
            if ([lienImageAuteur rangeOfString:@"http://"].location == NSNotFound && [lienImageAuteur rangeOfString:@"https://"].location == NSNotFound ){ //Si l'image ne provient pas d'autre site
                lienImageAuteur = [@"http://zestedesavoir.com" stringByAppendingString:lienImageAuteur];
                
            }
            //On crée l'image
            NSURL *url = [NSURL URLWithString:lienImageAuteur];
           
            [array addObject:url];
            
            if(![self findArrayInArray:_infosParserTopic Array:array]){
                [_infosParserTopic addObject:array];
                [self updateNotificationWithArray:array];
            }
           
            
            
        
        }
         array = nil;
    }
   
}


- (NSString *) parseString:(NSString *)string{
       BOOL save = NO;
   
    NSMutableString *stringResult = [[NSMutableString alloc] init];
    for (int i = 0; i < [string length]; i++){
        if (save){
            [stringResult appendFormat:@"%c",[string characterAtIndex:i]];
        }
        if ([string characterAtIndex:i] == '>'){
            save = YES;
            
        
        } else if ([string characterAtIndex:i] == '<'){
            save = NO;
        }
        
    }
   
    return[stringResult  stringByReplacingCharactersInRange:NSMakeRange([stringResult length]-1, 1) withString:@""];
 
}

-(void)startTimer{
    NSLog(@"%f",[PreferenceController preferenceRefresh]);
    timer = [NSTimer scheduledTimerWithTimeInterval:[PreferenceController preferenceRefresh]
                                             target:self
                                           selector:@selector(parseZDS)
                                           userInfo:nil
                                            repeats:YES];
}
-(void)stopTimer{
    [timer invalidate];
    timer = nil;
}
- (void)reStartTimer{
    [self stopTimer];
    [self startTimer];
}
- (BOOL) isConnextionZDS{
    
    HTMLNode *bodyNode = [parser body];
    NSArray *nodes = [bodyNode findChildTags:@"div"]; //On cherche tout les blocs <li>
    if (bodyNode == nil){
        return NO;
    }
    for (HTMLNode *nodeHTML in nodes ){
        if ([[nodeHTML getAttributeNamed:@"class"] isEqualToString:@"logbox header-right unlogged"]){
            return NO;
        }
    }
    
    
    return YES;
}

- (NSString *)getPseudosZDS{
    
    HTMLNode *body = [parser body];
    NSArray *node = [body findChildTags:@"span"];
    NSString *pseudos;
    for (HTMLNode *nodeHtml in node){
        if([[nodeHtml getAttributeNamed:@"class"] isEqualToString:@"username label"]){
            pseudos =[[NSString alloc] initWithString:[self parseString:[nodeHtml rawContents]]];
            
        }
    }
    return pseudos;
}


- (NSURL *)getUrlImagePseudos{
    HTMLNode *body = [parser body];
    NSArray *node = [body findChildTags:@"img"];
    NSString *urlPseudos;
    for (HTMLNode *nodeHtml in node )
        if ([[nodeHtml getAttributeNamed:@"class"] isEqualToString:@"avatar"]){
            urlPseudos = [[NSString alloc] initWithFormat:@"%@",[nodeHtml getAttributeNamed:@"src"]];
        }
    
    
    
    if ([urlPseudos rangeOfString:@"http://"].location == NSNotFound && [urlPseudos rangeOfString:@"https://"].location == NSNotFound ){ //Si l'image ne provient pas d'autre site
            return [NSURL URLWithString:[@"http://zestedesavoir.com" stringByAppendingString:urlPseudos]];
        
    }else{
        return [NSURL URLWithString:urlPseudos];
    }
    
    
}


-(BOOL)findArrayInArray:(NSMutableArray *)arrayInArray Array:(NSMutableArray *)arrayValue{
    BOOL findIsYes = NO;
    
    for (NSMutableArray *arrayIn in arrayInArray){
        
        if ([[arrayIn objectAtIndex:2] isEqualTo:[arrayValue objectAtIndex:2]]){ //C'est le lien qui fait toute la difference
            findIsYes = YES;
            
            
        }
        
    }
    return findIsYes;
}

//---------
//Notification
//----------

-(void)updateNotificationWithArray: (NSMutableArray *)array{
    NSLog(@"Envoie une notification");
    NSNotificationCenter *nc = [NSNotificationCenter defaultCenter];
    NSDictionary *d = [NSDictionary dictionaryWithObject:array forKey:@"ZDSParser"];
    [nc postNotificationName:ZDSUpdateNotification object:nil userInfo:d];
}

@end
