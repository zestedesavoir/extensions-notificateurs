//
//  ConnectonParse.m
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 28/08/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//

#import "ConnectonParse.h"
#import "HTMLParser.h"
#import "HTMLNode.h"
@implementation ConnectonParse

- (id) init{
    self = [super init];
    if (self){
        NSURL *tutorialsUrl = [NSURL URLWithString:@"http://zestedesavoir.com"];
        
        NSData *tutorialsHtmlData = [NSData dataWithContentsOfURL:tutorialsUrl];
        NSError *error = nil;
        parser = [[HTMLParser alloc] initWithData:tutorialsHtmlData error:&error]; //On parse
    }
    return self;
}


- (BOOL) isConnextionZDS{
    
    HTMLNode *bodyNode = [parser body];
    NSArray *nodes = [bodyNode findChildTags:@"div"]; //On cherche tout les blocs <li>
    
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


- (NSString *)getUrlImagePseudos{
    HTMLNode *body = [parser body];
    NSArray *node = [body findChildTags:@"img"];
    NSString *urlPseudos;
    for (HTMLNode *nodeHtml in node )
    if ([[nodeHtml getAttributeNamed:@"class"] isEqualToString:@"avatar"]){
     urlPseudos = [[NSString alloc] initWithFormat:@"%@",[nodeHtml getAttributeNamed:@"src"]];
            }
    
    
    if ([urlPseudos rangeOfString:@"http://"].location == NSNotFound && [urlPseudos rangeOfString:@"https://"].location == NSNotFound ){ //Si l'image ne provient pas d'autre site
        return [@"http://zestedesavoir.com" stringByAppendingString:urlPseudos];
        
    }else{
        return urlPseudos;
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
       // NSLog(@"string result = %@",[stringResult  stringByReplacingCharactersInRange:NSMakeRange([stringResult length]-1, 1) withString:@""]);
    return[stringResult  stringByReplacingCharactersInRange:NSMakeRange([stringResult length]-1, 1) withString:@""];
    
}

@end
