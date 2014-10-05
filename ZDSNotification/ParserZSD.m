//
//  ParserZSD.m
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 26/08/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//

#import "ParserZSD.h"

@class HTMLNode;
@class HTMLParser;

@implementation ParserZSD



- (id) init{
    self = [super init];
    
    if (self){
        _infosParserTopic = [[NSMutableArray alloc]init];
        article = [[NSMutableArray alloc] init];
    }
    return self;
}

- (NSMutableArray *)parseZDS{
    // Example : file:///Users/Odric/Documents/ZDSNotificationHtml.html
    // Exampe : file:///Users/Odric/Documents/ZDSAvec1Message.html
    // Lien du site :http://zestedesavoir.com
    
    NSURL *tutorialsUrl = [NSURL URLWithString:@"http://zestedesavoir.com"];
    
    NSData *tutorialsHtmlData = [NSData dataWithContentsOfURL:tutorialsUrl];
    NSError *error = nil;
    HTMLParser *parser = [[HTMLParser alloc] initWithData:tutorialsHtmlData error:&error];
    
    
    
    
    
    if (error){
        return nil;
    }
    //Partie Topic
    HTMLNode *bodyNode = [parser body];
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
            
            [array addObject:lienImageAuteur];
            
            [_infosParserTopic addObject:array];
            
        }
    }
    return _infosParserTopic;
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

@end
